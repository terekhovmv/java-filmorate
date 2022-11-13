package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.assertj.core.api.Assertions.*;

public abstract class BaseFilmStorageTest {
    private FilmStorageTestHelper testeeHelper;
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
        UserStorageTestHelper userStorageHelper = new UserStorageTestHelper(getUserStorage());

        for (int id = 1; id <= INITIAL_FILM_COUNT; id++) {
            testeeHelper.addFilm(id);
        }
        for (int id = 1; id <= INITIAL_USER_COUNT; id++) {
            userStorageHelper.addUser(id);
        }
    }

    @Test
    public void testContainsByKnownId() {
        for (int id = 1; id <= INITIAL_FILM_COUNT; id++) {
            assertThat(getTestee().contains(id)).isTrue();

            final int finalId = id;
            assertThatCode(()->getTestee().requireContains(finalId))
                    .doesNotThrowAnyException();
        }
    }

    @Test
    public void testContainsByUnknownId() {
        var id = INITIAL_FILM_COUNT + 1;
        assertThat(getTestee().contains(id)).isFalse();
        assertThatExceptionOfType(FilmNotFoundException.class)
                .isThrownBy(() -> getTestee().requireContains(id))
                .withMessage("Unable to find the film #%d", id);
    }

    @Test
    public void testGetByKnownId() {
        for (int id = 1; id <= INITIAL_FILM_COUNT; id++) {
            Film expected = testeeHelper.getExpectedFilm(id, 0);
            assertThat(getTestee().get(id))
                    .isPresent()
                    .hasValueSatisfying(found -> assertThat(found)
                            .isEqualTo(expected));

            final int finalId = id;
            assertThatCode(()->getTestee().requireContains(finalId))
                    .doesNotThrowAnyException();
        }
    }

    @Test
    public void testGetWithRate() {
        int id = 2;
        long ann = 1;
        long bob = 2;
        long calvin = 3;

        LikesStorage likesStorage = getLikesStorage();
        likesStorage.addLike(id, ann);
        likesStorage.addLike(id, bob);
        likesStorage.addLike(id, calvin);

        Film expected = testeeHelper.getExpectedFilm(id, 3);
        assertThat(getTestee().get(id))
                .isPresent()
                .hasValueSatisfying(found -> assertThat(found)
                        .isEqualTo(expected));
    }

    @Test
    public void testGetByUnknownId() {
        var id = INITIAL_FILM_COUNT + 1;
        assertThat(getTestee().get(id)).isEmpty();
        assertThatExceptionOfType(FilmNotFoundException.class)
                .isThrownBy(() -> getTestee().require(id))
                .withMessage("Unable to find the film #%d", id);
    }

    @Test
    public void testGetAll() {
        Film[] expected = new Film[INITIAL_FILM_COUNT];
        for (int id = 1; id <= INITIAL_FILM_COUNT; id++) {
            expected[id-1] = testeeHelper.getExpectedFilm(id, 0);
        }

        var result = getTestee().getAll();
        assertThat(result)
                .hasSize(INITIAL_FILM_COUNT)
                .contains(expected);
    }

    @Test
    public void testGetAllWithRate() {
        int idWithRate = 3;
        long ann = 1;
        long bob = 2;
        long calvin = 3;

        LikesStorage likesStorage = getLikesStorage();
        likesStorage.addLike(idWithRate, ann);
        likesStorage.addLike(idWithRate, bob);
        likesStorage.addLike(idWithRate, calvin);

        Film[] expected = new Film[INITIAL_FILM_COUNT];
        for (int id = 1; id <= INITIAL_FILM_COUNT; id++) {
            expected[id-1] = testeeHelper.getExpectedFilm(id, id == idWithRate ? 3 : 0);
        }

        var result = getTestee().getAll();
        assertThat(result)
                .hasSize(INITIAL_FILM_COUNT)
                .contains(expected);
    }
}
