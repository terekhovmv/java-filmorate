package ru.yandex.practicum.filmorate.storage.friends;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryFriendsStorage implements FriendsStorage {
    private final Map<Long, Set<Long>> storage = new HashMap<>();

    @Override
    public boolean addToUserFriends(long userId, long friendId) {
        boolean result = add(userId, friendId);
        boolean symmetricResult = add(friendId, userId);
        assert result == symmetricResult;
        return result;
    }

    @Override
    public boolean deleteFromUserFriends(long userId, long friendId) {
        boolean result = delete(userId, friendId);
        boolean symmetricResult = delete(friendId, userId);
        assert result == symmetricResult;
        return result;
    }

    @Override
    public List<Long> getUserFriends(long userId) {
        Set<Long> friends = storage.get(userId);
        if (friends == null) {
            return List.of();
        }
        return new ArrayList<>(friends);
    }

    private boolean add(long userId, long friendId) {
        storage.putIfAbsent(userId, new HashSet<>());
        return storage.get(userId).add(friendId);
    }

    private boolean delete(long userId, long friendId) {
        Set<Long> friends = storage.get(userId);
        if (friends == null) {
            return false;
        }
        return friends.remove(friendId);
    }
}
