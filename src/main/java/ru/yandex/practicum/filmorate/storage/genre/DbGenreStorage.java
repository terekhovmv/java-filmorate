package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.*;
import java.util.List;
import java.util.stream.Stream;

@Component
@Qualifier("db")
public class DbGenreStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> rowMapper;

    public DbGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = new GenreRowMapper();
    }

    @Override
    public boolean contains(short id) {
        SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT id FROM genre WHERE id=?;", id);
        return result.next();
    }

    @Override
    public Genre getById(short id) {
        List<Genre> found = jdbcTemplate.query(
                "SELECT * FROM genres WHERE id=?;",
                this.rowMapper,
                id
        );
        if (found.size() == 0) {
            throw new GenreNotFoundException(id);
        }

        return found.get(0);
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
