package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.storage.BaseFriendshipStorageTest;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbFriendshipStorageTest extends BaseFriendshipStorageTest {
    private final DbFriendshipStorage testee;
    private final DbUserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM friendship");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("ALTER TABLE users ALTER COLUMN id RESTART WITH 1;");
    }

    @Override
    protected FriendshipStorage getTestee() {
        return testee;
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
