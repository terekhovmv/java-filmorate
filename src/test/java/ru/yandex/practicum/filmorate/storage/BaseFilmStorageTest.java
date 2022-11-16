package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public abstract class BaseFilmStorageTest {
    private FilmStorageTestHelper testeeHelper;
    private static final int INITIAL_FILM_COUNT = 5;
    private static final int INITIAL_USER_COUNT = 5;

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
            Film expected = testeeHelper.getExpectedFilmWithRate(id, 0);
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

        Film expected = testeeHelper.getExpectedFilmWithRate(id, 3);
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
            expected[id-1] = testeeHelper.getExpectedFilmWithRate(id, 0);
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
            expected[id-1] = testeeHelper.getExpectedFilmWithRate(id, id == idWithRate ? 3 : 0);
        }

        var result = getTestee().getAll();
        assertThat(result)
                .hasSize(INITIAL_FILM_COUNT)
                .contains(expected);
    }

    @Test
    public void testCreate() {
        var expected = testeeHelper.getExpectedFilmWithRate(INITIAL_FILM_COUNT + 1, 0);
        var archetype = expected.toBuilder().id(null).build();

        assertThat(getTestee().create(archetype))
                .isPresent()
                .hasValueSatisfying(created -> assertThat(created)
                        .isEqualTo(expected));
    }

    @Test
    public void testUpdate() {
        int id = INITIAL_FILM_COUNT;
        long ann = 1;
        long bob = 2;
        long calvin = 3;
        LikesStorage likesStorage = getLikesStorage();
        likesStorage.addLike(id, ann);
        likesStorage.addLike(id, bob);
        likesStorage.addLike(id, calvin);

        var from = testeeHelper.getExpectedFilmNoRate(id, id + 1);
        var expected = testeeHelper.getExpectedFilmWithRate(id, id + 1, 3);

        assertThat(getTestee().update(from))
                .isPresent()
                .hasValueSatisfying(updated -> assertThat(updated)
                        .isEqualTo(expected));
    }

    @Test
    public void testUpdateByUnknownId() {
        var from = testeeHelper.getExpectedFilmNoRate(INITIAL_FILM_COUNT + 1);

        assertThat(getTestee().update(from)).isEmpty();
    }

    @Test
    public void testGetPopularNoPopular() {
        assertThat(getTestee().getPopular(3))
                .hasSameElementsAs(List.of(
                        testeeHelper.getExpectedFilmWithRate(INITIAL_FILM_COUNT, 0),
                        testeeHelper.getExpectedFilmWithRate(INITIAL_FILM_COUNT-1, 0),
                        testeeHelper.getExpectedFilmWithRate(INITIAL_FILM_COUNT-2, 0))
                );
    }


    @Test
    public void testGetPopular() {
        int amelie = 1;
        int batman = 2;
        long ann = 1;
        long bob = 2;
        long calvin = 3;
        LikesStorage likesStorage = getLikesStorage();
        likesStorage.addLike(amelie, ann);
        likesStorage.addLike(amelie, bob);
        likesStorage.addLike(amelie, calvin);
        likesStorage.addLike(batman, bob);
        likesStorage.addLike(batman, calvin);

        assertThat(getTestee().getPopular(5))
                .hasSameElementsAs(List.of(
                        testeeHelper.getExpectedFilmWithRate(amelie, 3),
                        testeeHelper.getExpectedFilmWithRate(batman, 2),
                        testeeHelper.getExpectedFilmWithRate(INITIAL_FILM_COUNT, 0),
                        testeeHelper.getExpectedFilmWithRate(INITIAL_FILM_COUNT-1, 0),
                        testeeHelper.getExpectedFilmWithRate(INITIAL_FILM_COUNT-2, 0))
                );
    }
}
