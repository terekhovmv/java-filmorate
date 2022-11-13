package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

@Component
@Qualifier(DbStorageConsts.QUALIFIER)
public class DbLikesStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    public DbLikesStorage(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean contains(int filmId, long userId) {
        SqlRowSet result = jdbcTemplate.queryForRowSet(
                "SELECT * FROM likes WHERE film_id=? AND user_id=?;",
                filmId,
                userId
        );
        return result.next();
    }


    @Override
    public void addLike(int filmId, long userId) {
        jdbcTemplate.update(
            "MERGE INTO likes (film_id, user_id) KEY(film_id, user_id) VALUES (?, ?);",
                filmId,
                userId
        );
    }

    @Override
    public void deleteLike(int filmId, long userId) {
        jdbcTemplate.update(
                "DELETE FROM likes WHERE film_id=? AND user_id=?;",
                filmId,
                userId
        );
    }
}
