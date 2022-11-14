package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseLikesStorageTest {
    private static final int INITIAL_FILM_COUNT = 10;
    private static final int INITIAL_USER_COUNT = 10;

    protected abstract FilmStorage getFilmStorage();
    protected abstract UserStorage getUserStorage();
    protected abstract LikesStorage getTestee();

    protected void beforeEach() {
        FilmStorageTestHelper filmStorageHelper = new FilmStorageTestHelper(getFilmStorage());
        UserStorageTestHelper userStorageHelper = new UserStorageTestHelper(getUserStorage());

        for (int id = 1; id <= INITIAL_FILM_COUNT; id++) {
            filmStorageHelper.addFilm(id);
        }
        for (int id = 1; id <= INITIAL_USER_COUNT; id++) {
            userStorageHelper.addUser(id);
        }
    }

    @Test
    void testAddLike() {
        var testee = getTestee();

        final int amelie = 1;
        final long ann = 2;

        assertThat(testee.contains(amelie, ann)).isFalse();

        testee.addLike(amelie, ann);
        assertThat(testee.contains(amelie, ann)).isTrue();
    }

    @Test
    void testDeleteLike() {
        var testee = getTestee();

        final int amelie = 1;
        final long ann = 2;

        testee.addLike(amelie, ann);
        testee.deleteLike(amelie, ann);

        assertThat(testee.contains(amelie, ann)).isFalse();
    }

    @Test
    void testGetLikesCountNoFilmLikes() {
        var testee = getTestee();

        final int amelie = 1;

        assertThat(testee.getLikesCount(amelie)).isZero();
    }

    @Test
    void testGetLikesCountWithoutDuplication() {
        var testee = getTestee();

        final int amelie = 1;
        final long ann = 1;
        final long bob = 2;

        testee.addLike(amelie, ann);
        testee.addLike(amelie, ann);
        testee.addLike(amelie, bob);

        assertThat(testee.getLikesCount(amelie)).isEqualTo(2);
    }

    @Test
    void testGetLikesCount() {
        var testee = getTestee();

        final int amelie = 1;
        final int batman = 2;
        final long ann = 1;
        final long bob = 2;
        final long calvin = 3;

        testee.addLike(amelie, ann);
        testee.addLike(amelie, calvin);
        testee.addLike(batman, bob);

        assertThat(testee.getLikesCount(amelie)).isEqualTo(2);
        assertThat(testee.getLikesCount(batman)).isEqualTo(1);
    }
}
