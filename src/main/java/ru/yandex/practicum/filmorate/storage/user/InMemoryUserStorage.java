package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@Qualifier("in-memory")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> storage = new HashMap<>();
    private final Map<Long, Set<Long>> friendshipStorage = new HashMap<>();

    private long lastId = 0;

    @Override
    public boolean contains(long id) {
        return storage.containsKey(id);
    }

    @Override
    public User getById(long id) {
        requireContains(id);

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
        requireContains(id);

        storage.put(id, from);
        return from;
    }

    @Override
    public boolean addToUserFriends(long userId, long friendId) {
        friendshipStorage.putIfAbsent(userId, new HashSet<>());
        return friendshipStorage.get(userId).add(friendId);
    }

    @Override
    public boolean deleteFromUserFriends(long userId, long friendId) {
        Set<Long> friends = friendshipStorage.get(userId);
        if (friends == null) {
            return false;
        }
        return friends.remove(friendId);
    }

    @Override
    public List<User> getUserFriends(long userId) {
        Set<Long> friends = friendshipStorage.get(userId);
        if (friends == null) {
            return List.of();
        }
        return friends.stream().map((id)->storage.get(id)).collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonUserFriends(long userId, long otherUserId) {
        Set<Long> intersection = new HashSet<Long>(
                Objects.requireNonNullElse(
                        friendshipStorage.get(userId),
                        Set.of()
                )
        );
        intersection.retainAll(
                Objects.requireNonNullElse(
                        friendshipStorage.get(otherUserId),
                        Set.of()
                )
        );
        return intersection.stream().map((id)->storage.get(id)).collect(Collectors.toList());
    }
}
