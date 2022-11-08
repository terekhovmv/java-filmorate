package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
@Qualifier("user-storage")
public class DbUserStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = new UserRowMapper();
    }

    @Override
    public boolean contains(long id) {
        SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT id FROM users WHERE id=?;", id);
        return result.next();
    }

    @Override
    public User getById(long id) {
        List<User> foundUsers = jdbcTemplate.query(
                "SELECT * FROM users WHERE id=?;",
                this.userRowMapper,
                id
        );
        if (foundUsers.size() == 0) {
            throw new UserNotFoundException(id);
        }

        return foundUsers.get(0);
    }

    @Override
    public Stream<User> stream() {
        return jdbcTemplate.query(
                "SELECT * FROM users;",
                this.userRowMapper
        ).stream();
    }

    @Override
    public User create(User archetype) {
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
        Objects.requireNonNull(id, "Unexpected issue on adding user " + archetype.getLogin());
        return getById(id.longValue());
    }

    @Override
    public User update(User from) {
        requireContains(from.getId());

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

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id(rs.getLong("id"))
                    .email(rs.getString("email"))
                    .login(rs.getString("login"))
                    .name(rs.getString("name"))
                    .login(rs.getString("login"))
                    .birthday(rs.getDate("birthday").toLocalDate())
                    .build();
        }
    }
}
