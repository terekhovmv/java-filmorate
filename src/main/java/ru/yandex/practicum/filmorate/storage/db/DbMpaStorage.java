package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier(DbStorageConsts.QUALIFIER)
public class DbMpaStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public DbMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> getById(short id) {
        List<Mpa> found = jdbcTemplate.query(
                "SELECT * FROM mpa WHERE id=?;",
                this::buildMpa,
                id
        );
        if (found.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(found.get(0));
    }

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM mpa;",
                this::buildMpa
        );
    }

    private Mpa buildMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(
                rs.getShort("id"),
                rs.getString("name")
        );
    }
}
