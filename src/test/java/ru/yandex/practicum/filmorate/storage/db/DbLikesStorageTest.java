package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.storage.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbLikesStorageTest extends BaseLikesStorageTest {
    private final DbLikesStorage testee;
    private final DbFilmStorage filmStorage;
    private final DbUserStorage userStorage;

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
