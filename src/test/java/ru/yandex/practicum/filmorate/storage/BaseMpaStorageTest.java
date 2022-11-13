package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Mpa;

import static org.assertj.core.api.Assertions.assertThat;


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
        }
    }

    @Test
    public void testContainsByUnknownId() {
        assertThat(getTestee().contains(UNKNOWN_ID)).isFalse();
    }

    @Test
    public void testGetByKnownId() {
        for (var wellKnownItem: WELL_KNOWN) {
            assertThat(getTestee().get(wellKnownItem.getId()))
                    .isPresent()
                    .hasValueSatisfying(found -> assertThat(found)
                            .hasFieldOrPropertyWithValue("id",wellKnownItem.getId())
                            .hasFieldOrPropertyWithValue("name", wellKnownItem.getName())
                    );

        }
    }

    @Test
    public void testGetByUnknownId() {
        assertThat(getTestee().get((short)777)).isEmpty();
    }

    @Test
    public void testGetAll() {
        var result = getTestee().getAll();
        assertThat(result)
                .hasSizeGreaterThan(0)
                .contains(WELL_KNOWN);
    }
}
