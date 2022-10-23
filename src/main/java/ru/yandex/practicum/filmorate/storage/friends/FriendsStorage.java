package ru.yandex.practicum.filmorate.storage.friends;

import java.util.List;

public interface FriendsStorage {
    boolean addToUserFriends(long userId, long friendId);
    boolean deleteFromUserFriends(long userId, long friendId);
    List<Long> getUserFriends(long userId);
}
