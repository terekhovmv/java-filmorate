package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserStorageTestHelper {
    private final UserStorage storage;

    public UserStorageTestHelper(UserStorage storage) {
        this.storage = storage;
    }

    public void addUser(int idx) {
        storage.create(User.builder()
                .email(createUserEmail(idx))
                .login(createUserLogin(idx))
                .name(createUserName(idx))
                .birthday(createUserBirthday(idx))
                .build()
        );
    }

    public User getExpectedUser(long id, int idx) {
        return User.builder()
                .id(id)
                .email(createUserEmail(idx))
                .login(createUserLogin(idx))
                .name(createUserName(idx))
                .birthday(createUserBirthday(idx))
                .build();
    }

    private String createUserEmail(int idx) {
        return idx + "@mail.com";
    }

    private String createUserLogin(int idx) {
        return idx + "login";
    }

    private String createUserName(int idx) {
        return idx + "name";
    }

    private LocalDate createUserBirthday(int idx) {
        return LocalDate.of(1980, 1, idx);
    }
}
