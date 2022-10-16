package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.exceptions.UnknownItem;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;

@Slf4j
public class InMemoryUserStorage extends AbstractStorage<User> {
    @Override
    protected int getId(User stored) {
        return stored.getId();
    }

    @Override
    protected User build(int id, User archetype) {
        User.UserBuilder builder = archetype.toBuilder().id(id);
        Optional<String> nameToSet = getNameToSet(archetype);
        nameToSet.ifPresent(builder::name);
        return builder.build();
    }

    @Override
    protected User buildForUpdate(User from) {
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

    @Override
    protected void onUnknown(int id) throws UnknownItem {
        String message = String.format("Unknown user %d requested", id);
        log.warn(message);
        throw new UnknownItem(message);
    }

    @Override
    protected void onAfterCreate(User item) {
        log.info("User {} was successfully added with id {}", item.getLogin(), item.getId());
    }

    @Override
    protected void onAfterUpdate(User item) {
        log.info("User {} was successfully updated", item.getLogin());
    }
}
