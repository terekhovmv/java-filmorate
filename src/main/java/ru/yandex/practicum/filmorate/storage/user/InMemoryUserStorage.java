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
    public Optional<User> getById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Stream<User> stream() {
        return storage.values().stream();
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

    @Override
    public boolean addFriend(long id, long friendId) {
        friendshipStorage.putIfAbsent(id, new HashSet<>());
        return friendshipStorage.get(id).add(friendId);
    }

    @Override
    public boolean deleteFriend(long id, long friendId) {
        Set<Long> friends = friendshipStorage.get(id);
        if (friends == null) {
            return false;
        }
        return friends.remove(friendId);
    }

    @Override
    public Stream<User> getFriends(long id) {
        Set<Long> friends = friendshipStorage.get(id);
        if (friends == null) {
            return Stream.empty();
        }
        return friends.stream().map((userId)->storage.get(userId));
    }

    @Override
    public Stream<User> getCommonFriends(long id, long otherId) {
        Set<Long> intersection = new HashSet<Long>(
                Objects.requireNonNullElse(
                        friendshipStorage.get(id),
                        Set.of()
                )
        );
        intersection.retainAll(
                Objects.requireNonNullElse(
                        friendshipStorage.get(otherId),
                        Set.of()
                )
        );
        return intersection.stream().map((userId)->storage.get(userId));
    }
}
