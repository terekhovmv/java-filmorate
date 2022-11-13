package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import static org.assertj.core.api.Assertions.*;

public abstract class BaseUserStorageTest {
    private UserStorageTestHelper testeeHelper;
    private static final int INITIAL_COUNT = 10;

    protected abstract UserStorage getTestee();

    protected void beforeEach() {
        testeeHelper = new UserStorageTestHelper(getTestee());
        for (int idx = 1; idx <= INITIAL_COUNT; idx++) {
            testeeHelper.addUser(idx);
        }
    }

    @Test
    public void testContainsByKnownId() {
        for (int idx = 1; idx <= INITIAL_COUNT; idx++) {
            final long id = idx;
            assertThat(getTestee().contains(id)).isTrue();
            assertThatCode(()->getTestee().requireContains(id))
                    .doesNotThrowAnyException();
        }
    }

    @Test
    public void testContainsByUnknownId() {
        var id = INITIAL_COUNT + 1;
        assertThat(getTestee().contains(id)).isFalse();
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> getTestee().requireContains(id))
                .withMessage("Unable to find the user #%d", id);
    }

    @Test
    public void testGetByKnownId() {
        for (int idx = 1; idx <= INITIAL_COUNT; idx++) {
            final long id = idx;
            User expected = testeeHelper.getExpectedUser(id, idx);
            assertThat(getTestee().get(id))
                    .isPresent()
                    .hasValueSatisfying(found -> assertThat(found)
                            .isEqualTo(expected));
            assertThatCode(()->getTestee().requireContains(id))
                    .doesNotThrowAnyException();
        }
    }

    @Test
    public void testGetByUnknownId() {
        var id = INITIAL_COUNT + 1;
        assertThat(getTestee().get(id)).isEmpty();
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> getTestee().require(id))
                .withMessage("Unable to find the user #%d", id);
    }

    @Test
    public void testGetAll() {
        User[] expected = new User[INITIAL_COUNT];
        for (int idx = 0; idx < INITIAL_COUNT; idx++) {
            expected[idx] = testeeHelper.getExpectedUser(idx+1, idx+1);
        }

        var result = getTestee().getAll();
        assertThat(result)
                .hasSize(INITIAL_COUNT)
                .contains(expected);
    }

    @Test
    public void testCreate() {
        var expected = testeeHelper.getExpectedUser(INITIAL_COUNT+1, INITIAL_COUNT+1);
        var archetype = expected.toBuilder().id(null).build();

        assertThat(getTestee().create(archetype))
                .isPresent()
                .hasValueSatisfying(created -> assertThat(created)
                        .isEqualTo(expected));
    }

    @Test
    public void testUpdate() {
        var from = testeeHelper.getExpectedUser(INITIAL_COUNT, INITIAL_COUNT+1);

        assertThat(getTestee().update(from))
                .isPresent()
                .hasValueSatisfying(updated -> assertThat(updated)
                        .isEqualTo(from));
    }

    @Test
    public void testUpdateByUnknownId() {
        var from = testeeHelper.getExpectedUser(INITIAL_COUNT+1, INITIAL_COUNT+1);

        assertThat(getTestee().update(from)).isEmpty();
    }
}
