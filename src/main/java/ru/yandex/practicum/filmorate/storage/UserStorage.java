package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.exceptions.UnknownItem;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
public class UserStorage extends AbstractStorage<User> {
    @Override
    protected int getId(User stored) {
        return stored.getId();
    }

    @Override
    protected User create(int id, User archetype) {
        User created = archetype.toBuilder().id(id).build();
        makeValid(created);
        return created;
    }

    @Override
    protected User update(User prev, User from) {
        makeValid(from);
        return from;
    }

    void makeValid(User value) {
        String name = value.getName();
        if (name == null || name.isBlank()) {
            value.setName(value.getLogin());
        }
    }

    @Override
    protected void onUnknown(int id) throws UnknownItem {
        String message = String.format("Unknown user %d requested", id);
        log.warn(message);
        throw new UnknownItem(message);
    }

    @Override
    protected void onAfterCreate(User created) {
        log.info("User {} was successfully added with id {}", created.getLogin(), created.getId());
    }

    @Override
    protected void onAfterUpdate(User updated) {
        log.info("User {} was successfully updated", updated.getLogin());
    }
}
