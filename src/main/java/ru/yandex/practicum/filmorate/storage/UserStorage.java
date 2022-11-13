package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    boolean contains(long id);
    Optional<User> get(long id);
    default void requireContains(long id) {
        if (!contains(id)) {
            throw new UserNotFoundException(id);
        }
    }
    default User require(long id) {
        return get(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    List<User> getAll();
    Optional<User> create(User archetype);
    Optional<User> update(User from);
}
