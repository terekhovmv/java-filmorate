package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.StorageQualifiers;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(
            @Qualifier(StorageQualifiers.DEFAULT_USER_STORAGE_QUALIFIER)
            UserStorage userStorage
    ) {
        this.userStorage = userStorage;
    }

    public User getById(long id) {
        return userStorage.getById(id);
    }

    public List<User> getAll() {
        return userStorage.stream().collect(Collectors.toList());
    }

    public User create(User archetype) {
        User item = userStorage.create(correctUserIfNeeded(archetype));
        log.info("User {} was successfully added with id {}", item.getLogin(), item.getId());
        return item;
    }

    public User update(User from) {
        User item = userStorage.update(correctUserIfNeeded(from));
        log.info("User {} was successfully updated", item.getId());
        return item;
    }

    public void addToUserFriends(long id, long friendId) {
        userStorage.requireContains(id);
        userStorage.requireContains(friendId);

        if (userStorage.addToUserFriends(id, friendId)) {
            log.info("User {} was successfully set as friend for {}", friendId, id);
        } else {
            log.info("User {} is already friend for {}", friendId, id);
        }
    }

    public void deleteFromUserFriends(long id, long friendId) {
        userStorage.requireContains(id);
        userStorage.requireContains(friendId);

        if (userStorage.deleteFromUserFriends(id, friendId)) {
            log.info("User {} was successfully unfriended for {}", friendId, id);
        } else {
            log.info("Unable to unfriend the user {} for {}, since they are not friends", friendId, id);
        }
    }

    public List<User> getUserFriends(long id) {
        userStorage.requireContains(id);

        return userStorage.getUserFriends(id);
    }

    public List<User> getCommonUserFriends(long id, long otherId) {
        userStorage.requireContains(id);
        userStorage.requireContains(otherId);

        return userStorage.getCommonUserFriends(id, otherId);
    }

    private User correctUserIfNeeded(User item) {
        String name = item.getName();
        if (name != null && !name.isBlank()) {
            return item;
        }

        return item.toBuilder().name(item.getLogin()).build();
    }
}
