package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public UserService(
            UserStorage userStorage,
            FriendsStorage friendsStorage
    ) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public User getById(long id) {
        return userStorage.getById(id);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User archetype) {
        User item = userStorage.create(correctUserIfNeeded(archetype));
        log.info("User {} was successfully added with id {}", item.getLogin(), item.getId());
        return item;
    }

    public User update(User from) {
        User item = userStorage.update(correctUserIfNeeded(from));
        log.info("User {} was successfully updated", item.getId());
        return item;
    }

    public void delete(long id) {
        User item = userStorage.delete(id);
        log.info("User {} (login='{}') was successfully deleted", item.getId(), item.getLogin());
    }

    private User correctUserIfNeeded(User item) {
        String name = item.getName();
        if (name != null && !name.isBlank()) {
            return item;
        }

        return item.toBuilder().name(item.getLogin()).build();
    }
}
