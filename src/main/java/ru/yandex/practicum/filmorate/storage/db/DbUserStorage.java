package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier(DbStorageConsts.QUALIFIER)
public class DbUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> rowMapper;

    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new DbUserRowMapper();
    }

    @Override
    public Optional<User> getById(long id) {
        List<User> found = jdbcTemplate.query(
                "SELECT * FROM users WHERE id=?;",
                this.rowMapper,
                id
        );
        if (found.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(found.get(0));
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users;",
                this.rowMapper
        );
    }

    @Override
    public Optional<User> create(User archetype) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?) ",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    statement.setString(1, archetype.getEmail());
                    statement.setString(2, archetype.getLogin());
                    statement.setString(3, archetype.getName());
                    statement.setDate(4, Date.valueOf(archetype.getBirthday()));
                    return statement;
                },
                generatedKeyHolder
        );
        Number id = generatedKeyHolder.getKey();
        if (id == null) {
            return Optional.empty();
        }
        return getById(id.longValue());
    }

    @Override
    public Optional<User> update(User from) {
        jdbcTemplate.update(
                "UPDATE users SET email=?, login=?, name=?, birthday=? WHERE id=?;",
                from.getEmail(),
                from.getLogin(),
                from.getName(),
                from.getBirthday(),
                from.getId()
        );
        return getById(from.getId());
    }
}
