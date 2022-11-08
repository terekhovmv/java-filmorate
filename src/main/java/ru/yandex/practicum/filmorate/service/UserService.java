package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public UserService(
            @Qualifier("user-storage") UserStorage userStorage,
            FriendsStorage friendsStorage
    ) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
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

        if (friendsStorage.addToUserFriends(id, friendId)) {
            log.info("User {} was successfully set as friend for {}", friendId, id);
        } else {
            log.info("User {} is already friend for {}", friendId, id);
        }
    }

    public void deleteFromUserFriends(long id, long friendId) {
        userStorage.requireContains(id);
        userStorage.requireContains(friendId);

        if (friendsStorage.deleteFromUserFriends(id, friendId)) {
            log.info("User {} was successfully unfriended for {}", friendId, id);
        } else {
            log.info("Unable to unfriend the user {} for {}, since they are not friends", friendId, id);
        }
    }

    public List<User> getUserFriends(long id) {
        userStorage.requireContains(id);

        List<Long> friendIds = friendsStorage.getUserFriends(id);
        return getByIds(friendIds);
    }

    public List<User> getCommonUserFriends(long id, long otherId) {
        userStorage.requireContains(id);
        userStorage.requireContains(otherId);

        List<Long> friendIds = friendsStorage.getUserFriends(id);
        List<Long> otherFriendIds = friendsStorage.getUserFriends(otherId);

        List<Long> commonIds = new ArrayList<>();
        for(Long friendId: friendIds) {
            if (otherFriendIds.contains(friendId)) {
                commonIds.add(friendId);
            }
        }
        return getByIds(commonIds);
    }

    private User correctUserIfNeeded(User item) {
        String name = item.getName();
        if (name != null && !name.isBlank()) {
            return item;
        }

        return item.toBuilder().name(item.getLogin()).build();
    }

    private List<User> getByIds(List<Long> ids) {
        List<User> result = new ArrayList<>();
        for(Long id: ids) {
            if (userStorage.contains(id)) {
                result.add(userStorage.getById(id));
            }
        }

        return result;
    }
}
