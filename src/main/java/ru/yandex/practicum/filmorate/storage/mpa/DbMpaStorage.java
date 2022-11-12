package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.*;
import java.util.List;
import java.util.stream.Stream;

@Component
@Qualifier("db")
public class DbMpaStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Mpa> rowMapper;

    public DbMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = new MpaRowMapper();
    }

    @Override
    public boolean contains(short id) {
        SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT id FROM mpa WHERE id=?;", id);
        return result.next();
    }

    @Override
    public Mpa getById(short id) {
        List<Mpa> found = jdbcTemplate.query(
                "SELECT * FROM mpa WHERE id=?;",
                this.rowMapper,
                id
        );
        if (found.size() == 0) {
            throw new MpaNotFoundException(id);
        }

        return found.get(0);
    }

    @Override
    public Stream<Mpa> stream() {
        return jdbcTemplate.query(
                "SELECT * FROM mpa;",
                this.rowMapper
        ).stream();
    }

    private static class MpaRowMapper implements RowMapper<Mpa> {
        @Override
        public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Mpa.builder()
                    .id(rs.getShort("id"))
                    .name(rs.getString("name"))
                    .build();
        }
    }
}
