package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryStorageQualifiers;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Stream;

@Component
@Qualifier(InMemoryStorageQualifiers.USER)
public class InMemoryFriendshipStorage implements FriendshipStorage {
    private final UserStorage userStorage;
    private final Map<Long, Set<Long>> storage = new HashMap<>();

    public InMemoryFriendshipStorage(
            @Qualifier(InMemoryStorageQualifiers.USER)
            UserStorage userStorage
    ) {
        this.userStorage = userStorage;
    }

    @Override
    public boolean addFriend(long id, long friendId) {
        storage.putIfAbsent(id, new HashSet<>());
        return storage.get(id).add(friendId);
    }

    @Override
    public boolean deleteFriend(long id, long friendId) {
        Set<Long> friends = storage.get(id);
        if (friends == null) {
            return false;
        }
        return friends.remove(friendId);
    }

    @Override
    public Stream<User> getFriends(long id) {
        Set<Long> friends = storage.get(id);
        if (friends == null) {
            return Stream.empty();
        }
        return getUsersStream(friends);
    }

    @Override
    public Stream<User> getCommonFriends(long id, long otherId) {
        Set<Long> friends = storage.get(id);
        if (friends == null) {
            return Stream.empty();
        }
        Set<Long> otherFriends = storage.get(otherId);
        if (otherFriends == null) {
            return Stream.empty();
        }

        Set<Long> intersection = new HashSet<>(friends);
        intersection.retainAll(otherFriends);
        return getUsersStream(intersection);
    }

    private Stream<User> getUsersStream(Collection<Long> ids) {
        return ids.stream().map((id) -> userStorage.getById(id).get());
    }
}
