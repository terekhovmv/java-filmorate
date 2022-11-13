package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseFriendshipStorageTest {
    private UserStorageTestHelper userStorageHelper;
    private static final int INITIAL_USER_COUNT = 10;

    protected abstract UserStorage getUserStorage();
    protected abstract FriendshipStorage getTestee();

    protected void beforeEach() {
        userStorageHelper = new UserStorageTestHelper(getUserStorage());
        for (int idx = 1; idx <= INITIAL_USER_COUNT; idx++) {
            userStorageHelper.addUser(idx);
        }
    }

    @Test
    void testAddFriend() {
        var testee = getTestee();

        final int id = 1;
        final int friendId = 2;

        assertThat(testee.contains(id, friendId)).isFalse();

        testee.addFriend(id, friendId);
        assertThat(testee.contains(id, friendId)).isTrue();
    }

    @Test
    void testDeleteFriend() {
        var testee = getTestee();

        final int ann = 1;
        final int bob = 2;

        testee.addFriend(ann, bob);
        testee.deleteFriend(ann, bob);

        assertThat(testee.contains(ann, bob)).isFalse();
    }

    @Test
    void testGetFriendsOfKnownUser() {
        var testee = getTestee();

        final int ann = 1;
        final int bob = 2;
        final int calvin = 3;

        assertThat(testee.getFriends(ann)).isEmpty();

        testee.addFriend(ann, bob);
        testee.addFriend(ann, calvin);

        assertThat(testee.getFriends(ann))
                .hasSize(2)
                .contains(
                        userStorageHelper.getExpectedUser(bob),
                        userStorageHelper.getExpectedUser(calvin));
    }

    @Test
    void testGetCommonFriendsNoIntersection() {
        var testee = getTestee();

        final int ann = 1;
        final int bob = 2;
        final int calvin = 3;
        final int dionis = 4;

        testee.addFriend(ann, bob);
        testee.addFriend(ann, calvin);
        testee.addFriend(bob, dionis);

        assertThat(testee.getCommonFriends(ann, bob)).isEmpty();
    }

    @Test
    void testGetCommonFriends() {
        var testee = getTestee();

        final int ann = 1;
        final int bob = 2;
        final int calvin = 3;
        final int dionis = 4;
        final int elrond = 5;

        testee.addFriend(ann, bob);
        testee.addFriend(ann, calvin);
        testee.addFriend(ann, dionis);
        testee.addFriend(bob, calvin);
        testee.addFriend(bob, dionis);
        testee.addFriend(bob, elrond);

        assertThat(testee.getCommonFriends(ann, bob))
                .hasSize(2)
                .contains(
                        userStorageHelper.getExpectedUser(calvin),
                        userStorageHelper.getExpectedUser(dionis));
    }
}
