package ru.yandex.practicum.filmorate.storage.friends;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryFriendsStorage implements FriendsStorage {
    private final Map<Long, Set<Long>> storage = new HashMap<>();

    @Override
    public void addToUserFriends(long userId, long friendId) {
        add(userId, friendId);
        add(friendId, userId);
    }

    @Override
    public void deleteFromUserFriends(long userId, long friendId) {
        delete(userId, friendId);
        delete(friendId, userId);
    }

    @Override
    public List<Long> getUserFriends(long userId) {
        Set<Long> friends = storage.get(userId);
        if (friends == null) {
            return List.of();
        }
        return new ArrayList<>(friends);
    }

    private void add(long userId, long friendId) {
        storage.putIfAbsent(userId, new HashSet<>());
        storage.get(userId).add(friendId);
    }

    private void delete(long userId, long friendId) {
        Set<Long> friends = storage.get(userId);
        if (friends == null) {
            return;
        }
        friends.remove(friendId);
    }
}
