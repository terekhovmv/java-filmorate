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
        for (int id = 1; id <= INITIAL_COUNT; id++) {
            testeeHelper.addUser(id);
        }
    }

    @Test
    public void testContainsByKnownId() {
        for (int id = 1; id <= INITIAL_COUNT; id++) {
            assertThat(getTestee().contains(id)).isTrue();

            final long finalId = id;
            assertThatCode(()->getTestee().requireContains(finalId))
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
        for (int id = 1; id <= INITIAL_COUNT; id++) {
            User expected = testeeHelper.getExpectedUser(id);
            assertThat(getTestee().get(id))
                    .isPresent()
                    .hasValueSatisfying(found -> assertThat(found)
                            .isEqualTo(expected));

            final long finalId = id;
            assertThatCode(()->getTestee().requireContains(finalId))
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
        for (int id = 1; id <= INITIAL_COUNT; id++) {
            expected[id-1] = testeeHelper.getExpectedUser(id);
        }

        var result = getTestee().getAll();
        assertThat(result)
                .hasSize(INITIAL_COUNT)
                .contains(expected);
    }

    @Test
    public void testCreate() {
        var expected = testeeHelper.getExpectedUser(INITIAL_COUNT+1);
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
        var from = testeeHelper.getExpectedUser(INITIAL_COUNT+1);

        assertThat(getTestee().update(from)).isEmpty();
    }
}
