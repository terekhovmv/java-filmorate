package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import static org.assertj.core.api.Assertions.*;

public abstract class BaseGenreStorageTest {
    private static final Genre[] WELL_KNOWN = {
            new Genre((short) 1, "Комедия"),
            new Genre((short) 3, "Мультфильм"),
            new Genre((short) 5, "Приключения")
    };
    private static final short UNKNOWN_ID = 777;

    protected abstract GenreStorage getTestee();

    @Test
    public void testContainsByKnownId() {
        for (var wellKnownItem : WELL_KNOWN) {
            assertThat(getTestee().contains(wellKnownItem.getId())).isTrue();
            assertThatCode(()->getTestee().requireContains(wellKnownItem.getId()))
                    .doesNotThrowAnyException();
        }
    }

    @Test
    public void testContainsByUnknownId() {
        assertThat(getTestee().contains(UNKNOWN_ID)).isFalse();
        assertThatExceptionOfType(GenreNotFoundException.class)
                .isThrownBy(() -> getTestee().requireContains(UNKNOWN_ID))
                .withMessage("Unable to find the genre #%d", UNKNOWN_ID);
    }

    @Test
    public void testGetByKnownId() {
        for (var wellKnownItem : WELL_KNOWN) {
            assertThat(getTestee().get(wellKnownItem.getId()))
                    .isPresent()
                    .hasValueSatisfying(found -> assertThat(found)
                            .isEqualTo(wellKnownItem));
            assertThatCode(()->getTestee().requireContains(wellKnownItem.getId()))
                    .doesNotThrowAnyException();
        }
    }

    @Test
    public void testGetByUnknownId() {
        assertThat(getTestee().get(UNKNOWN_ID)).isEmpty();
        assertThatExceptionOfType(GenreNotFoundException.class)
                .isThrownBy(() -> getTestee().require(UNKNOWN_ID))
                .withMessage("Unable to find the genre #%d", UNKNOWN_ID);
    }


    @Test
    public void testGetAll() {
        var result = getTestee().getAll();
        assertThat(result)
                .hasSizeGreaterThan(0)
                .contains(WELL_KNOWN);
    }
}