package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exceptions.UnknownItem;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> storage = new HashMap<>();
    private int lastId = 0;

    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    public User create(User archetype) {
        lastId++;
        User item = build(lastId, archetype);

        storage.put(lastId, item);
        log.info("User {} was successfully updated", item.getLogin());

        return item;
    }

    public User update(User from) {
        int id = from.getId();
        if (!storage.containsKey(id)) {
            onUnknown(id);
        }

        User item = buildForUpdate(from);
        storage.put(id, item);
        log.info("User {} was successfully updated", item.getLogin());

        return item;
    }

    public void delete(int id) {
        if (!storage.containsKey(id)) {
            onUnknown(id);
        }

        User item = storage.get(id);
        storage.remove(id);
        log.info("User {} was successfully deleted", item.getLogin());
    }

    private User build(int id, User archetype) {
        User.UserBuilder builder = archetype.toBuilder().id(id);
        Optional<String> nameToSet = getNameToSet(archetype);
        nameToSet.ifPresent(builder::name);
        return builder.build();
    }

    private User buildForUpdate(User from) {
        Optional<String> nameToSet = getNameToSet(from);
        if (nameToSet.isEmpty()) {
            return from;
        }
        return from.toBuilder().name(nameToSet.get()).build();
    }

    private Optional<String> getNameToSet(User user) {
        String value = user.getName();
        if (value == null || value.isBlank()) {
            return Optional.of(user.getLogin());
        }
        return Optional.empty();
    }

    private void onUnknown(int id) {
        String message = String.format("Unknown user %d requested", id);
        log.warn(message);
        throw new UnknownItem(message);
    }
}
