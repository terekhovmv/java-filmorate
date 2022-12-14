package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
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
    public boolean contains(long id) {
        SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT id FROM users WHERE id=?;", id);
        return result.next();
    }

    @Override
    public Optional<User> get(long id) {
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
        return get(id.longValue());
    }

    @Override
    public Optional<User> update(User from) {
        if (!contains(from.getId())) {
            return Optional.empty();
        }

        jdbcTemplate.update(
                "UPDATE users SET email=?, login=?, name=?, birthday=? WHERE id=?;",
                from.getEmail(),
                from.getLogin(),
                from.getName(),
                from.getBirthday(),
                from.getId()
        );
        return get(from.getId());
    }
}
