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
public class MpaStorageIntegrationTest {
    private final DbMpaStorage testee;
    private final Mpa[] wellKnownMpas = {
            Mpa.builder().id((short)1).name("G").build(),
            Mpa.builder().id((short)3).name("PG-13").build(),
            Mpa.builder().id((short)5).name("NC-17").build()
    };

    @Test
    public void testGetById() {
        for (Mpa mpa: wellKnownMpas) {
            assertThat(testee.getById(mpa.getId()))
                    .isPresent()
                    .hasValueSatisfying(found -> assertThat(found)
                            .hasFieldOrPropertyWithValue("id",mpa.getId())
                            .hasFieldOrPropertyWithValue("name", mpa.getName())

                    );

        }
    }
}
