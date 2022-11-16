package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserStorageTestHelper {
    private final UserStorage storage;

    public UserStorageTestHelper(UserStorage storage) {
        this.storage = storage;
    }

    public void addUser(int userFriendlyIdx) {
        storage.create(User.builder()
                .email(createUserEmail(userFriendlyIdx))
                .login(createUserLogin(userFriendlyIdx))
                .name(createUserName(userFriendlyIdx))
                .birthday(createUserBirthday(userFriendlyIdx))
                .build()
        );
    }

    public User getExpectedUser(long id, int userFriendlyIdx) {
        return User.builder()
                .id(id)
                .email(createUserEmail(userFriendlyIdx))
                .login(createUserLogin(userFriendlyIdx))
                .name(createUserName(userFriendlyIdx))
                .birthday(createUserBirthday(userFriendlyIdx))
                .build();
    }

    public User getExpectedUser(long id) {
        return getExpectedUser(id, (int)id);
    }

    private String createUserEmail(int userFriendlyIdx) {
        return userFriendlyIdx + "@mail.com";
    }

    private String createUserLogin(int userFriendlyIdx) {
        return userFriendlyIdx + "login";
    }

    private String createUserName(int userFriendlyIdx) {
        return userFriendlyIdx + "name";
    }

    private LocalDate createUserBirthday(int userFriendlyIdx) {
        return LocalDate.of(1980, 1, userFriendlyIdx);
    }
}
