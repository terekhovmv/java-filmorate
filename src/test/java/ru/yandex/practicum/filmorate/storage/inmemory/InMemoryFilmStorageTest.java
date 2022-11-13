package ru.yandex.practicum.filmorate.storage.inmemory;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.storage.*;

public class InMemoryFilmStorageTest extends BaseFilmStorageTest {
    private FilmStorage testee;
    private UserStorage userStorage;
    private LikesStorage likesStorage;
    private MpaStorage mpaStorage;
    private GenreStorage genreStorage;


    @BeforeEach
    protected void beforeEach() {
        userStorage = new InMemoryUserStorage();
        likesStorage = new InMemoryLikesStorage();
        mpaStorage = new InMemoryMpaStorage();
        genreStorage = new InMemoryGenreStorage();

        testee = new InMemoryFilmStorage(
                likesStorage,
                mpaStorage,
                genreStorage
        );
        super.beforeEach();
    }


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
}
