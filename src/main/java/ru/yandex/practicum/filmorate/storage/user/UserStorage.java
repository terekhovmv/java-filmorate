package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.stream.Stream;

public interface UserStorage {
    boolean contains(long id);
    User getById(long id);
    Stream<User> stream();
    User create(User archetype);
    User update(User from);
}
