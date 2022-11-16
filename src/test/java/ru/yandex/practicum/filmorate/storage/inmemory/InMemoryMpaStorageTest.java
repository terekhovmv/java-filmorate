package ru.yandex.practicum.filmorate.storage.inmemory;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.storage.BaseMpaStorageTest;
import ru.yandex.practicum.filmorate.storage.MpaStorage;


public class InMemoryMpaStorageTest extends BaseMpaStorageTest {
    private MpaStorage testee;

    @BeforeEach
    void beforeEach() {
        testee = new InMemoryMpaStorage();
    }

    @Override
    protected MpaStorage getTestee() {
        return testee;
    }
}
