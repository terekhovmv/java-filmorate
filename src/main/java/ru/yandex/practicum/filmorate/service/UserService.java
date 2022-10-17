package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public UserService(
            UserStorage userStorage,
            FriendsStorage friendsStorage
    ) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User archetype) {
        return userStorage.create(archetype);
    }

    public User update(User from) {
        return userStorage.update(from);
    }
}
