package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbMpaStorageIntegrationTest {
    private final DbMpaStorage testee;
    private static final Mpa[] WELL_KNOWN = {
            new Mpa((short)1, "G"),
            new Mpa((short)3, "PG-13"),
            new Mpa((short)5, "NC-17")
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
        assertThat(testee.get((short)777)).isEmpty();
    }

    @Test
    public void testGetAll() {
        var result = testee.getAll();
        assertThat(result)
                .hasSizeGreaterThan(0)
                .contains(WELL_KNOWN);
    }
}
