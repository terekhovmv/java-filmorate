package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.stream.Stream;

public interface UserStorage {
    boolean contains(long id);
    User getById(long id);
    Stream<User> stream();
    User create(User archetype);
    User update(User from);
    default void requireContains(long id) {
        if (!contains(id)) {
            throw new UserNotFoundException(id);
        }
    }

    boolean addToUserFriends(long userId, long friendId);
    boolean deleteFromUserFriends(long userId, long friendId);
    List<User> getUserFriends(long userId);
    List<User> getCommonUserFriends(long userId, long otherUserId);
}
