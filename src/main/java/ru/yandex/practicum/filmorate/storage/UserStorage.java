package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public class UserStorage extends AbstractStorage<User> {
    protected int getId(User stored) {
        return stored.getId();
    }

    protected User create(int id, User archetype) {
        return archetype.toBuilder().id(id).build();
    }
}
