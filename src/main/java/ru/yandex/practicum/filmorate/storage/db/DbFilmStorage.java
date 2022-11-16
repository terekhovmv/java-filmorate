package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier(DbStorageConsts.QUALIFIER)
public class DbFilmStorage implements FilmStorage {
    private final MpaStorage mpaStorage;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> genreRowMapper;

    public DbFilmStorage(
            @Qualifier(DbStorageConsts.QUALIFIER)
            MpaStorage mpaStorage,
            JdbcTemplate jdbcTemplate
    ){
        this.mpaStorage = mpaStorage;
        this.jdbcTemplate = jdbcTemplate;
        this.genreRowMapper = new DbGenreRowMapper();
    }

    @Override
    public boolean contains(int id) {
        SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT id FROM films WHERE id=?;", id);
        return result.next();
    }

    @Override
    public Optional<Film> get(int id) {
        List<Film> found = jdbcTemplate.query(
                "SELECT f.*, COUNT(l.user_id) AS rate \n" +
                    "FROM films as f \n" +
                    "LEFT JOIN likes AS l ON l.film_id=f.id \n" +
                    "WHERE f.id=? \n" +
                    "GROUP by f.id;",
                this::buildFilm,
                id
        );
        if (found.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(found.get(0));
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(
                "SELECT f.*, COUNT(l.user_id) AS rate \n" +
                "FROM films as f \n" +
                "LEFT JOIN likes AS l ON l.film_id=f.id \n" +
                "GROUP by f.id;",
                this::buildFilm
        );
    }

    @Override
    public List<Film> getPopular(int count) {
        return jdbcTemplate.query(
            "SELECT f.*, COUNT(l.user_id) AS rate \n" +
                "FROM films as f \n" +
                "LEFT JOIN likes AS l ON l.film_id=f.id \n" +
                "GROUP by f.id \n" +
                "ORDER BY rate DESC, f.id DESC \n" +
                "LIMIT ?; \n",
                this::buildFilm,
                count
        );
    }

    @Override
    public Optional<Film> create(Film archetype) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?) ",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    statement.setString(1, archetype.getName());
                    statement.setString(2, archetype.getDescription());
                    statement.setDate(3, Date.valueOf(archetype.getReleaseDate()));
                    statement.setInt(4, archetype.getDuration());
                    statement.setShort(5, archetype.getMpa().getId());
                    return statement;
                },
                generatedKeyHolder
        );
        Number id = generatedKeyHolder.getKey();
        if (id == null) {
            return Optional.empty();
        }

        int filmId = id.intValue();
        updateFilmGenres(
                filmId,
                null,
                archetype.getGenres()
                        .stream()
                        .map(Genre::getId)
                        .collect(Collectors.toList())
        );
        return get(filmId);
    }

    @Override
    public Optional<Film> update(Film from) {
        int filmId = from.getId();
        if (!contains(filmId)) {
            return Optional.empty();
        }

        jdbcTemplate.update(
                "UPDATE films SET name=?, description=?, release_date=?, duration=?, mpa_id=? WHERE id=?;",
                from.getName(),
                from.getDescription(),
                from.getReleaseDate(),
                from.getDuration(),
                from.getMpa().getId(),
                filmId
        );

        Set<Short> existentGenreIds = getFilmGenres(filmId)
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toSet()
        );
        Set<Short> genreIdsToAdd = from.getGenres()
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        Set<Short> genreIdsToDelete = new HashSet<>(existentGenreIds);
        genreIdsToDelete.removeAll(genreIdsToAdd);
        genreIdsToAdd.removeAll(existentGenreIds);

        updateFilmGenres(
                filmId,
                new ArrayList<>(genreIdsToDelete),
                new ArrayList<>(genreIdsToAdd)
        );

        return get(from.getId());
    }

    private List<Genre> getFilmGenres(int id) {
        return jdbcTemplate.query(
                "SELECT g.* " +
                "FROM film_genres as fg " +
                "LEFT JOIN genres as g ON fg.genre_id=g.id " +
                "WHERE fg.film_id=?;",
                this.genreRowMapper,
                id
        );
    }

    private void updateFilmGenres(int filmId, List<Short> genreIdsToDelete, List<Short> genreIdsToAdd) {
        if (genreIdsToDelete != null) {
            jdbcTemplate.batchUpdate(
                    "DELETE FROM film_genres WHERE film_id=? AND genre_id=?;",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, filmId);
                            ps.setShort(2, genreIdsToDelete.get(i));
                        }
                        @Override
                        public int getBatchSize() {
                            return genreIdsToDelete.size();
                        }
                    }
            );
        }

        if (genreIdsToAdd != null) {
            jdbcTemplate.batchUpdate(
                    "MERGE INTO film_genres (film_id, genre_id) KEY(film_id, genre_id) VALUES (?, ?);",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, filmId);
                            ps.setShort(2, genreIdsToAdd.get(i));
                        }
                        @Override
                        public int getBatchSize() {
                            return genreIdsToAdd.size();
                        }
                    }
            );
        }
    }

    public Film buildFilm(ResultSet rs, int rowNum) throws SQLException {
        int filmId = rs.getInt("id");
        return Film.builder()
                .id(filmId)
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate"))
                .mpa(mpaStorage
                        .get(rs.getShort("mpa_id"))
                        .orElse(null)
                )
                .genres(getFilmGenres(filmId))
                .build();
    }
}
