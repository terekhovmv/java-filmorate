package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbGenreStorageIntegrationTest {
    private final DbGenreStorage testee;
    private static final Genre[] WELL_KNOWN = {
            new Genre((short)1, "Комедия"),
            new Genre((short)3, "Мультфильм"),
            new Genre((short)5, "Приключения")
    };
    private static final short UNKNOWN_ID = 777;

    @Test
    public void testContainsByKnownId() {
        for (var wellKnownItem: WELL_KNOWN) {
            assertThat(testee.contains(wellKnownItem.getId())).isTrue();
        }
    }

    @Test
    public void testContainsByUnknownId() {
        assertThat(testee.contains(UNKNOWN_ID)).isFalse();
    }

    @Test
    public void testGetByKnownId() {
        for (var wellKnownItem: WELL_KNOWN) {
            assertThat(testee.get(wellKnownItem.getId()))
                    .isPresent()
                    .hasValueSatisfying(found -> assertThat(found)
                            .hasFieldOrPropertyWithValue("id",wellKnownItem.getId())
                            .hasFieldOrPropertyWithValue("name", wellKnownItem.getName())
                    );

        }
    }

    @Test
    public void testGetByUnknownId() {
        assertThat(testee.get(UNKNOWN_ID)).isEmpty();
    }

    @Test
    public void testGetAll() {
        var result = testee.getAll();
        assertThat(result)
                .hasSizeGreaterThan(0)
                .contains(WELL_KNOWN);
    }
}
