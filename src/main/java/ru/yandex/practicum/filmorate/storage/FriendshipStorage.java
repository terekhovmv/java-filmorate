package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {
    boolean contains(long id, long friendId);
    void addFriend(long id, long friendId);
    void deleteFriend(long id, long friendId);
    List<User> getFriends(long id);
    List<User> getCommonFriends(long id, long otherId);
}
