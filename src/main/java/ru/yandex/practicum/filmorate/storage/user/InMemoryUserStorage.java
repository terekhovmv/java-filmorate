package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exceptions.UnknownItem;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> storage = new HashMap<>();
    private long lastId = 0;

    @Override
    public boolean contains(long id) {
        return storage.containsKey(id);
    }

    @Override
    public User getById(long id) {
        checkIsKnown(id);

        return storage.get(id);
    }

    @Override
    public Stream<User> stream() {
        return storage.values().stream();
    }

    @Override
    public User create(User archetype) {
        lastId++;
        User item = archetype.toBuilder().id(lastId).build();

        storage.put(lastId, item);
        return item;
    }

    @Override
    public User update(User from) {
        long id = from.getId();
        checkIsKnown(id);

        storage.put(id, from);
        return from;
    }

    private void checkIsKnown(long id) {
        if (!contains(id)) {
            throw new UnknownItem(""); //TODO
        }
    }
}
