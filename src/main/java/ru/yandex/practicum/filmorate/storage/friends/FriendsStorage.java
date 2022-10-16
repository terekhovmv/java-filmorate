package ru.yandex.practicum.filmorate.storage.friends;

import java.util.List;

public interface FriendsStorage {
    void addToUserFriends(long userId, long friendId);
    void deleteFromUserFriends(long userId, long friendId);
    List<Long> getUserFriends(long userId);
}
