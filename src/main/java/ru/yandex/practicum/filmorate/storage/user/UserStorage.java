package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserStorage {
    Optional<User> getById(long id);
    Stream<User> stream();
    Optional<User> create(User archetype);
    Optional<User> update(User from);

    boolean addFriend(long id, long friendId);
    boolean deleteFriend(long id, long friendId);
    Stream<User> getFriends(long id);
    Stream<User> getCommonFriends(long id, long otherId);
}
