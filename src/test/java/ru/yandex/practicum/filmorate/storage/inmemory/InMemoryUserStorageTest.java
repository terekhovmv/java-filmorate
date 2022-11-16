package ru.yandex.practicum.filmorate.storage.inmemory;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.storage.BaseUserStorageTest;
import ru.yandex.practicum.filmorate.storage.UserStorage;


public class InMemoryUserStorageTest extends BaseUserStorageTest {
    private UserStorage testee;

    @BeforeEach
    protected void beforeEach() {
        testee = new InMemoryUserStorage();
        super.beforeEach();
    }

    @Override
    protected UserStorage getTestee() {
        return testee;
    }
}
