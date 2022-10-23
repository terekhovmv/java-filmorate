package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    boolean contains(long id);
    User getById(long id);
    List<User> getAll();
    User create(User archetype);
    User update(User from);
}
