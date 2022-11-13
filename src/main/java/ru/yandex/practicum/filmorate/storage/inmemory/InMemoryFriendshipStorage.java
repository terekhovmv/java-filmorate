package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier(InMemoryStorageConsts.QUALIFIER)
public class InMemoryFriendshipStorage implements FriendshipStorage {
    private final UserStorage userStorage;
    private final Map<Long, Set<Long>> storage = new HashMap<>();

    public InMemoryFriendshipStorage(
            @Qualifier(InMemoryStorageConsts.QUALIFIER)
            UserStorage userStorage
    ) {
        this.userStorage = userStorage;
    }

    @Override
    public boolean contains(long id, long friendId) {
        Set<Long> friends = storage.get(id);
        if (friends == null) {
            return false;
        }
        return friends.contains(friendId);
    }

    @Override
    public void addFriend(long id, long friendId) {
        storage.putIfAbsent(id, new HashSet<>());
        storage.get(id).add(friendId);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        Set<Long> friends = storage.get(id);
        if (friends == null) {
            return;
        }
        friends.remove(friendId);
    }

    @Override
    public List<User> getFriends(long id) {
        Set<Long> friends = storage.get(id);
        if (friends == null) {
            return List.of();
        }
        return getUsers(friends);
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        Set<Long> friends = storage.get(id);
        if (friends == null) {
            return List.of();
        }
        Set<Long> otherFriends = storage.get(otherId);
        if (otherFriends == null) {
            return List.of();
        }

        Set<Long> intersection = new HashSet<>(friends);
        intersection.retainAll(otherFriends);
        return getUsers(intersection);
    }

    private List<User> getUsers(Collection<Long> ids) {
        return ids
                .stream()
                .map((id) -> userStorage
                        .get(id)
                        .orElse(null))
                .filter(user -> user != null)
                .collect(Collectors.toList());
    }
}
