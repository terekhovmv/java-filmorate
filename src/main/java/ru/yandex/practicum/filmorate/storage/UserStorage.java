package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> getById(long id);
    List<User> getAll();
    Optional<User> create(User archetype);
    Optional<User> update(User from);
}
