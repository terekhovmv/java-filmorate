package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.storage.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbLikesStorageTest extends BaseLikesStorageTest {
    private final DbLikesStorage testee;
    private final DbFilmStorage filmStorage;
    private final DbUserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM likes");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM film_genres");
        jdbcTemplate.update("DELETE FROM films");
        jdbcTemplate.update("ALTER TABLE users ALTER COLUMN id RESTART WITH 1;");
        jdbcTemplate.update("ALTER TABLE films ALTER COLUMN id RESTART WITH 1;");
    }

    @Override
    protected LikesStorage getTestee() {
        return testee;
    }

    @Override
    protected FilmStorage getFilmStorage() {
        return filmStorage;
    }

    @Override
    protected UserStorage getUserStorage() {
        return userStorage;
    }

    @BeforeEach
    protected void beforeEach() {
        super.beforeEach();
    }
}
