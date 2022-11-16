package ru.yandex.practicum.filmorate.storage.inmemory;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.storage.BaseFriendshipStorageTest;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

public class InMemoryFriendshipStorageTest extends BaseFriendshipStorageTest {
    private FriendshipStorage testee;
    private UserStorage userStorage;


    @BeforeEach
    protected void beforeEach() {
        userStorage = new InMemoryUserStorage();
        testee = new InMemoryFriendshipStorage(userStorage);
        super.beforeEach();
    }

    @Override
    protected FriendshipStorage getTestee() {
        return testee;
    }

    @Override
    protected UserStorage getUserStorage() {
        return userStorage;
    }
}
