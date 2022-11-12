package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;
import java.util.stream.Stream;

public interface UserStorage {
    Optional<User> getById(long id);
    Stream<User> stream();
    Optional<User> create(User archetype);
    Optional<User> update(User from);
}