package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseGenreStorageTest {
    protected abstract GenreStorage getTestee();

    private static final Genre[] WELL_KNOWN = {
            new Genre((short) 1, "Комедия"),
            new Genre((short) 3, "Мультфильм"),
            new Genre((short) 5, "Приключения")
    };
    private static final short UNKNOWN_ID = 777;

    @Test
    public void testContainsByKnownId() {
        for (var wellKnownItem : WELL_KNOWN) {
            assertThat(getTestee().contains(wellKnownItem.getId())).isTrue();
        }
    }

    @Test
    public void testContainsByUnknownId() {
        assertThat(getTestee().contains(UNKNOWN_ID)).isFalse();
    }

    @Test
    public void testGetByKnownId() {
        for (var wellKnownItem : WELL_KNOWN) {
            assertThat(getTestee().get(wellKnownItem.getId()))
                    .isPresent()
                    .hasValueSatisfying(found -> assertThat(found)
                            .hasFieldOrPropertyWithValue("id", wellKnownItem.getId())
                            .hasFieldOrPropertyWithValue("name", wellKnownItem.getName())
                    );

        }
    }

    @Test
    public void testGetByUnknownId() {
        assertThat(getTestee().get(UNKNOWN_ID)).isEmpty();
    }

    @Test
    public void testGetAll() {
        var result = getTestee().getAll();
        assertThat(result)
                .hasSizeGreaterThan(0)
                .contains(WELL_KNOWN);
    }
}