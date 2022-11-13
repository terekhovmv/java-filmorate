package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import static org.assertj.core.api.Assertions.*;


public abstract class BaseMpaStorageTest {
    protected abstract MpaStorage getTestee();

    private static final Mpa[] WELL_KNOWN = {
            new Mpa((short)1, "G"),
            new Mpa((short)3, "PG-13"),
            new Mpa((short)5, "NC-17")
    };
    private static final short UNKNOWN_ID = 777;

    @Test
    public void testContainsByKnownId() {
        for (var wellKnownItem: WELL_KNOWN) {
            assertThat(getTestee().contains(wellKnownItem.getId())).isTrue();
            assertThatCode(()->getTestee().requireContains(wellKnownItem.getId()))
                    .doesNotThrowAnyException();
        }
    }

    @Test
    public void testContainsByUnknownId() {
        assertThat(getTestee().contains(UNKNOWN_ID)).isFalse();
        assertThatExceptionOfType(MpaNotFoundException.class)
                .isThrownBy(() -> getTestee().requireContains(UNKNOWN_ID))
                .withMessage("Unable to find MPA #%d", UNKNOWN_ID);
    }

    @Test
    public void testGetByKnownId() {
        for (var wellKnownItem: WELL_KNOWN) {
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
        assertThatExceptionOfType(MpaNotFoundException.class)
                .isThrownBy(() -> getTestee().require(UNKNOWN_ID))
                .withMessage("Unable to find MPA #%d", UNKNOWN_ID);
    }

    @Test
    public void testGetAll() {
        var result = getTestee().getAll();
        assertThat(result)
                .hasSizeGreaterThan(0)
                .contains(WELL_KNOWN);
    }
}
