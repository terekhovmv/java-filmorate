package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Component
@Qualifier(InMemoryStorageConsts.QUALIFIER)
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> storage = new HashMap<>();

    private long lastId = 0;

    @Override
    public Optional<User> getById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<User> create(User archetype) {
        lastId++;
        User item = archetype.toBuilder().id(lastId).build();

        storage.put(lastId, item);
        return Optional.of(item);
    }

    @Override
    public Optional<User> update(User from) {
        long id = from.getId();
        if (!storage.containsKey(id)) {
            return Optional.empty();
        }

        storage.put(id, from);
        return Optional.of(from);
    }
}
