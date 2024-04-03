package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.userFriendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.userFriendship.FriendshipStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendshipDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private FriendshipStorage friendshipStorage;
    private UserStorage userStorage;

    @BeforeEach
    public void beforeEach() {
        friendshipStorage = new FriendshipDbStorage(jdbcTemplate);
        userStorage = new UserDbStorage(jdbcTemplate, friendshipStorage);
    }

    @Test
    public void test_T0010_PS01_addToFriend() {
        User user1 = new User(1,
                "user1@email.ru",
                "vanya123",
                LocalDate.of(1995, 1, 1),
                "Ivan Petrov",
                new HashSet<>());

        User user2 = new User(2,
                "user2@email.ru",
                "maxim123",
                LocalDate.of(1990, 1, 1),
                "Maxim Ivanov",
                new HashSet<>());

        Integer user1Id = userStorage.addUser(user1);
        Integer user2Id = userStorage.addUser(user2);

        friendshipStorage.addToFriend(user1Id, user2Id);

        assertEquals(1, friendshipStorage.getUserFriendCounts(user1Id));
        assertEquals(0, friendshipStorage.getUserFriendCounts(user2Id));
    }

    @Test
    public void test_T0010_NS01_addToFriend_incorrectId() {
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> friendshipStorage.addToFriend(9999, 9999));
    }

    @Test
    public void test_T0020_PS01_deleteFromFriend() {
        User user1 = new User(1,
                "user1@email.ru",
                "vanya123",
                LocalDate.of(1995, 1, 1),
                "Ivan Petrov",
                new HashSet<>());

        User user2 = new User(2,
                "user2@email.ru",
                "maxim123",
                LocalDate.of(1990, 1, 1),
                "Maxim Ivanov",
                new HashSet<>());

        Integer user1Id = userStorage.addUser(user1);
        Integer user2Id = userStorage.addUser(user2);

        friendshipStorage.addToFriend(user1Id, user2Id);
        friendshipStorage.addToFriend(user2Id, user1Id);

        friendshipStorage.deleteFromFriend(user1Id, user2Id);

        assertEquals(0, friendshipStorage.getUserFriendCounts(user1Id));
        assertEquals(1, friendshipStorage.getUserFriendCounts(user2Id));
    }

    @Test
    public void test_T0020_NS01_deleteFromFriend_incorrectId() {
        assertFalse(friendshipStorage.deleteFromFriend(9999, 9999));
    }

    @Test
    public void test_T0030_NS01_getUserFriendCount_incorrectId() {
        assertEquals(0, userStorage.getUsersQuantity());
        assertEquals(0, friendshipStorage.getUserFriendCounts(9999));
    }

    @Test
    public void test_T0040_PS01_getCommonFriends() {
        User user1 = new User(1,
                "user1@email.ru",
                "vanya123",
                LocalDate.of(1995, 1, 1),
                "Ivan Petrov",
                new HashSet<>());

        User user2 = new User(2,
                "user2@email.ru",
                "maxim123",
                LocalDate.of(1990, 1, 1),
                "Maxim Ivanov",
                new HashSet<>());

        User user3 = new User(3,
                "user3@email.ru",
                "slava123",
                LocalDate.of(2000, 1, 1),
                "Slava Ivanov",
                new HashSet<>());

        Integer user1Id = userStorage.addUser(user1);
        Integer user2Id = userStorage.addUser(user2);
        Integer user3Id = userStorage.addUser(user3);

        friendshipStorage.addToFriend(user1Id, user2Id);
        friendshipStorage.addToFriend(user3Id, user2Id);

        List<User> common = friendshipStorage.getCommonFriendsAsUsers(user1Id, user3Id);

        assertEquals(1, common.size());
        assertEquals(user2, common.get(0));
    }

    @Test
    public void test_T0040_NS01_getCommonFriends_incorrectId() {
        assertEquals(0, userStorage.getUsersQuantity());
        assertEquals(0, friendshipStorage.getCommonFriendsAsUsers(9999, 9999).size());
    }

}