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
public class DbFilmStorageTest extends BaseFilmStorageTest {
    private final DbFilmStorage testee;
    private final DbUserStorage userStorage;
    private final DbLikesStorage likesStorage;
    private final DbMpaStorage mpaStorage;
    private final DbGenreStorage genreStorage;

    @Override
    protected FilmStorage getTestee() {
        return testee;
    }

    @Override
    protected UserStorage getUserStorage() {
        return userStorage;
    }

    @Override
    protected LikesStorage getLikesStorage() {
        return likesStorage;
    }

    @Override
    protected MpaStorage getMpaStorage() {
        return mpaStorage;
    }

    @Override
    protected GenreStorage getGenreStorage() {
        return genreStorage;
    }

    @BeforeEach
    protected void beforeEach() {
        super.beforeEach();
    }
}
