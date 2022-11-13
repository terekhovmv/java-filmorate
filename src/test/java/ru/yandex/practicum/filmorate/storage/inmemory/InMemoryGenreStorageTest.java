package ru.yandex.practicum.filmorate.storage.inmemory;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.storage.BaseGenreStorageTest;
import ru.yandex.practicum.filmorate.storage.GenreStorage;


public class InMemoryGenreStorageTest extends BaseGenreStorageTest {
    private GenreStorage testee;

    @BeforeEach
    void beforeEach() {
        testee = new InMemoryGenreStorage();
    }

    @Override
    protected GenreStorage getTestee() {
        return testee;
    }
}
