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
        return archetype.toBuilder().id(id).build();
    }

    @Override
    protected void onUnknown(int id) throws UnknownItem {
        String message = String.format("Unknown user %d requested", id);
        log.warn(message);
        throw new UnknownItem(message);
    }
}
