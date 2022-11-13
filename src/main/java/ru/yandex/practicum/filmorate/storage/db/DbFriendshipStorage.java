package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.stream.Stream;

@Component
@Qualifier(DbStorageConsts.QUALIFIER)
public class DbFriendshipStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> rowMapper;

    public DbFriendshipStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new DbUserRowMapper();
    }

    @Override
    public void addFriend(long id, long friendId) {
        jdbcTemplate.update(
                "MERGE INTO friendship (user_id, friend_id) KEY(user_id, friend_id) VALUES (?, ?);",
                id,
                friendId
        );
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        jdbcTemplate.update(
                "DELETE FROM friendship WHERE user_id=? AND friend_id=?;",
                id,
                friendId
        );
    }

    @Override
    public Stream<User> getFriends(long id) {
        final String query =
                "SELECT users.* \n" +
                "FROM users RIGHT JOIN ( \n" +
                "   SELECT DISTINCT friend_id AS id FROM friendship WHERE user_id=? \n" +
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
                "SELECT users.* \n" +
                "FROM users RIGHT JOIN ( \n" +
                "   SELECT DISTINCT friend_id AS id FROM friendship WHERE user_id=? \n" +
                "   INTERSECT \n" +
                "   SELECT DISTINCT friend_id AS id FROM friendship WHERE user_id=? \n" +
                ") AS found ON users.id=found.id;";

        return jdbcTemplate.query(
                query,
                this.rowMapper,
                id,
                otherUserId
        ).stream();
    }
}
