package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.stream.Stream;

public interface FriendshipStorage {
    void addFriend(long id, long friendId);
    void deleteFriend(long id, long friendId);
    Stream<User> getFriends(long id);
    Stream<User> getCommonFriends(long id, long otherId);
}
