package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.DbStorageQualifiers;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Qualifier(DbStorageQualifiers.GENRE)
public class DbGenreStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> rowMapper;

    public DbGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = new GenreRowMapper();
    }

    @Override
    public Optional<Genre> getById(short id) {
        List<Genre> found = jdbcTemplate.query(
                "SELECT * FROM genres WHERE id=?;",
                this.rowMapper,
                id
        );
        if (found.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(found.get(0));
    }

    @Override
    public Stream<Genre> stream() {
        return jdbcTemplate.query(
                "SELECT * FROM genres;",
                this.rowMapper
        ).stream();
    }

    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Genre.builder()
                    .id(rs.getShort("id"))
                    .name(rs.getString("name"))
                    .build();
        }
    }
}
