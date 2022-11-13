package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DefaultStorageConsts;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    public UserService(
            @Qualifier(DefaultStorageConsts.QUALIFIER)
            UserStorage userStorage,
            @Qualifier(DefaultStorageConsts.QUALIFIER)
            FriendshipStorage friendshipStorage
    ) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public User getById(long id) {
        return requireUser(id);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User archetype) {
        User corrected = correctUserIfNeeded(archetype);
        User created = userStorage.create(corrected).orElseThrow(
                () -> new RuntimeException("Unable to create the user " + corrected.getLogin()));
        log.info("User {} was successfully added with id {}", created.getLogin(), created.getId());
        return created;
    }

    public User update(User from) {
        requireUser(from.getId());

        User corrected = correctUserIfNeeded(from);
        User updated = userStorage.update(corrected).orElseThrow(
                () -> new RuntimeException("Unable to update the user #" + corrected.getId()));
        log.info("User {} was successfully added with id {}", updated.getLogin(), updated.getId());
        return updated;
    }

    public void addFriend(long id, long friendId) {
        requireUser(id);
        requireUser(friendId);

        if (friendshipStorage.addFriend(id, friendId)) {
            log.info("User {} was successfully set as friend for {}", friendId, id);
        } else {
            log.info("User {} is already friend for {}", friendId, id);
        }
    }

    public void deleteFriend(long id, long friendId) {
        requireUser(id);
        requireUser(friendId);

        if (friendshipStorage.deleteFriend(id, friendId)) {
            log.info("User {} was successfully unfriended for {}", friendId, id);
        } else {
            log.info("Unable to unfriend the user {} for {}, since they are not friends", friendId, id);
        }
    }

    public List<User> getFriends(long id) {
        requireUser(id);

        return friendshipStorage.getFriends(id).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId) {
        requireUser(id);
        requireUser(otherId);

        return friendshipStorage.getCommonFriends(id, otherId).collect(Collectors.toList());
    }

    private User correctUserIfNeeded(User item) {
        String name = item.getName();
        if (name != null && !name.isBlank()) {
            return item;
        }

        return item.toBuilder().name(item.getLogin()).build();
    }

    private User requireUser(long id) {
        return userStorage.getById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
