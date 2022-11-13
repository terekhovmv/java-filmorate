package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseUserStorageTest {
    protected abstract UserStorage getTestee();

    private static final short INITIAL_COUNT = 10;

    protected void beforeEach() {
        for (int idx = 1; idx <= INITIAL_COUNT; idx++) {
            addUser(idx);
        }
    }

    @Test
    public void testContainsByKnownId() {
        for (long id = 1; id <= INITIAL_COUNT; id++) {
            assertThat(getTestee().contains(id)).isTrue();
        }
    }

    @Test
    public void testContainsByUnknownId() {
        assertThat(getTestee().contains(INITIAL_COUNT + 1)).isFalse();
    }

    @Test
    public void testGetByKnownId() {
        for (int idx = 1; idx <= INITIAL_COUNT; idx++) {
            final long id = idx;
            User expected = getExpectedUser(id, idx);
            assertThat(getTestee().get(id))
                    .isPresent()
                    .hasValueSatisfying(found -> assertThat(found)
                            .isEqualTo(expected));

        }
    }

    @Test
    public void testGetByUnknownId() {
        assertThat(getTestee().get(INITIAL_COUNT + 1)).isEmpty();
    }

    void addUser(int idx) {
        getTestee().create(User.builder()
                .email(createUserEmail(idx))
                .login(createUserLogin(idx))
                .name(createUserName(idx))
                .birthday(createUserBirthday(idx))
                .build()
        );
    }

    User getExpectedUser(long id, int idx) {
        return User.builder()
                .id(id)
                .email(createUserEmail(idx))
                .login(createUserLogin(idx))
                .name(createUserName(idx))
                .birthday(createUserBirthday(idx))
                .build();
    }

    String createUserEmail(int idx) {
        return idx + "@mail.com";
    }

    String createUserLogin(int idx) {
        return idx + "login";
    }

    String createUserName(int idx) {
        return idx + "name";
    }

    LocalDate createUserBirthday(int idx) {
        return LocalDate.of(1980, 1, idx);
    }
}
