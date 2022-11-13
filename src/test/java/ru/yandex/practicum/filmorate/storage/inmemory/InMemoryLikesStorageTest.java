package ru.yandex.practicum.filmorate.storage.inmemory;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.storage.*;

public class InMemoryLikesStorageTest extends BaseLikesStorageTest {
    private LikesStorage testee;
    private FilmStorage filmStorage;
    private UserStorage userStorage;


    @BeforeEach
    protected void beforeEach() {
        testee = new InMemoryLikesStorage();
        filmStorage = new InMemoryFilmStorage(
                new InMemoryLikesStorage(),
                new InMemoryMpaStorage(),
                new InMemoryGenreStorage()
        );
        userStorage = new InMemoryUserStorage();
        super.beforeEach();
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
}