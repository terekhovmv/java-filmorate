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
    private final Genre[] wellKnownItems = {
            new Genre((short)1, "Комедия"),
            new Genre((short)3, "Мультфильм"),
            new Genre((short)5, "Приключения")
    };

    @Test
    public void testGetByKnownId() {
        for (var wellKnownItem: wellKnownItems) {
            assertThat(testee.getById(wellKnownItem.getId()))
                    .isPresent()
                    .hasValueSatisfying(found -> assertThat(found)
                            .hasFieldOrPropertyWithValue("id",wellKnownItem.getId())
                            .hasFieldOrPropertyWithValue("name", wellKnownItem.getName())
                    );

        }
    }

    @Test
    public void testGetByUnknownId() {
        assertThat(testee.getById((short)777)).isEmpty();
    }

    @Test
    public void testGetAll() {
        var result = testee.getAll();
        assertThat(result)
                .hasSizeGreaterThan(0)
                .contains(wellKnownItems);
    }
}
