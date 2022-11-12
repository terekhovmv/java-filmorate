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
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Qualifier("db")
public class DbUserStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> rowMapper;

    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new UserRowMapper();
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
    public Stream<User> stream() {
        return jdbcTemplate.query(
                "SELECT * FROM users;",
                this.rowMapper
        ).stream();
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

    @Override
    public boolean addFriend(long id, long friendId) {
        int rowsAffected = jdbcTemplate.update(
                "INSERT INTO friendship (user_id, friend_id) VALUES (?, ?);",
                id,
                friendId
        );
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteFriend(long id, long friendId) {
        int rowsAffected = jdbcTemplate.update(
                "DELETE FROM friendship WHERE user_id=? AND friend_id=?;",
                id,
                friendId
        );
        return rowsAffected > 0;
    }

    @Override
    public Stream<User> getFriends(long id) {
        final String query =
                "SELECT users.* " +
                "FROM users RIGHT JOIN ( " +
                "   SELECT DISTINCT friend_id AS id FROM friendship WHERE user_id=? " +
                ") AS found ON users.id=found.id;";

        return jdbcTemplate.query(
                query,
                this.rowMapper,
                id
        ).stream();
    }

    @Override
    public Stream<User> getCommonFriends(long id, long otherUserId) {
        final String query =
                "SELECT users.* " +
                "FROM users RIGHT JOIN ( " +
                "   SELECT DISTINCT friend_id AS id FROM friendship WHERE user_id=? " +
                "   INTERSECT " +
                "   SELECT DISTINCT friend_id AS id FROM friendship WHERE user_id=? " +
                ") AS found ON users.id=found.id;";

        return jdbcTemplate.query(
                query,
                this.rowMapper,
                id,
                otherUserId
        ).stream();
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
