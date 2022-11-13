package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;

import java.util.List;

public abstract class BaseFilmStorageTest {
    private FilmStorageTestHelper testeeHelper;
    private UserStorageTestHelper userStorageHelper;
    private static final int INITIAL_FILM_COUNT = 10;
    private static final int INITIAL_USER_COUNT = 10;

    protected abstract FilmStorage getTestee();

    protected abstract UserStorage getUserStorage();
    protected abstract LikesStorage getLikesStorage();
    protected abstract MpaStorage getMpaStorage();
    protected abstract GenreStorage getGenreStorage();

    protected void beforeEach() {
        testeeHelper = new FilmStorageTestHelper(
                getTestee(),
                getMpaStorage(),
                getGenreStorage()
        );
        userStorageHelper = new UserStorageTestHelper(getUserStorage());

        for (int idx = 1; idx <= INITIAL_FILM_COUNT; idx++) {
            testeeHelper.addFilm(idx);
        }
        for (int idx = 1; idx <= INITIAL_USER_COUNT; idx++) {
            userStorageHelper.addUser(idx);
        }
    }

    @Test
    void test() {

    }
}
