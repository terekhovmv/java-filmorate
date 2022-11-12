package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbStorageQualifiers;
import ru.yandex.practicum.filmorate.storage.user.DbUserRowMapper;

import java.util.stream.Stream;

@Component
@Qualifier(DbStorageQualifiers.USER)
public class DbFriendshipStorage implements FriendshipStorage{
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> rowMapper;

    public DbFriendshipStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new DbUserRowMapper();
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
}
