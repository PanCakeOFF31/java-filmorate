package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private FriendshipStorage friendshipStorage;
    private UserStorage userStorage;

    @BeforeEach
    public void beforeEach() {
        friendshipStorage = new FriendshipDbStorage(jdbcTemplate);
        userStorage = new UserDbStorage(jdbcTemplate, friendshipStorage);
    }

    @Test
    public void test_T0010_PS01_getUserById() {
        User newUser = new User(1,
                "user@email.ru",
                "vanya123",
                LocalDate.of(1990, 1, 1),
                "Ivan Petrov",
                new HashSet<>());

        int addedUserId = userStorage.addUser(newUser);

        assertNotNull(addedUserId);
        assertTrue(addedUserId > 0);

        User savedUser = userStorage.getUserById(addedUserId);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void test_T0010_NS01_getUserById_unknownUser() {
        assertThrows(org.springframework.dao.EmptyResultDataAccessException.class, () -> userStorage.getUserById(9999));
    }

    @Test
    public void test_T0020_PS01_getUsersQuantity() {
        User user1 = new User(1,
                "user1@email.ru",
                "vanya123",
                LocalDate.of(1990, 1, 1),
                "Ivan Petrov",
                new HashSet<>());

        User user2 = new User(user1);
        user2.setEmail("user2@email.ru");
        user2.setLogin("ivan123");

        Integer addedUser1Id = userStorage.addUser(user1);
        Integer addedUser2Id = userStorage.addUser(user2);

        assertNotNull(addedUser1Id);
        assertTrue(addedUser1Id > 0);
        assertNotNull(addedUser2Id);
        assertTrue(addedUser2Id > 0);

        assertEquals(2, userStorage.getUsersQuantity());
    }

    @Test
    public void test_T0030_PS01_getUsers() {
        assertEquals(0, userStorage.getUsersQuantity());

        User user1 = new User(1,
                "user1@email.ru",
                "vanya123",
                LocalDate.of(1990, 1, 1),
                "Ivan Petrov",
                new HashSet<>());

        User user2 = new User(user1);
        user2.setId(2);
        user2.setEmail("user2@email.ru");
        user2.setLogin("ivan123");

        userStorage.addUser(user1);
        userStorage.addUser(user2);
        assertEquals(2, userStorage.getUsersQuantity());

        List<User> users = userStorage.getUsers();
        assertEquals(2, users.size());

        assertEquals(user1.getEmail(), users.get(0).getEmail());
        assertEquals(user2.getEmail(), users.get(1).getEmail());
    }

    @Test
    public void test_T0040_PS01_addUser() {
        assertEquals(0, userStorage.getUsersQuantity());

        User user1 = new User(1,
                "user1@email.ru",
                "vanya123",
                LocalDate.of(1990, 1, 1),
                "Ivan Petrov",
                new HashSet<>());

        Integer addedId = userStorage.addUser(user1);
        User savedUser = userStorage.getUserById(addedId);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user1);

        assertEquals(1, userStorage.getUsersQuantity());
    }

    @Test
    public void test_T0040_NS01_addUser_emailLoginUniqueConstraint() {
        assertEquals(0, userStorage.getUsersQuantity());

        User user1 = new User(1,
                "user1@email.ru",
                "vanya123",
                LocalDate.of(1990, 1, 1),
                "Ivan Petrov",
                new HashSet<>());

        userStorage.addUser(user1);

        assertThrows(org.springframework.dao.DuplicateKeyException.class, () -> userStorage.addUser(user1));
    }

    @Test
    public void test_T0040_NS02_addUser_noNameCheckConstraint() {
        assertEquals(0, userStorage.getUsersQuantity());

        User user = new User(1,
                "user@email.ru",
                "vanya123",
                LocalDate.of(1990, 1, 1),
                "",
                new HashSet<>());

        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> userStorage.addUser(user));
    }

    @Test
    public void test_T0050_PS01_updateUser() {
        assertEquals(0, userStorage.getUsersQuantity());

        User user = new User(1,
                "user@email.ru",
                "vanya123",
                LocalDate.of(1990, 1, 1),
                "Ivan Petrov",
                new HashSet<>());

        int id = userStorage.addUser(user);
        assertEquals(1, userStorage.getUsersQuantity());

        User changedUser = new User(id,
                "user@email.ru",
                "IVAN_123",
                LocalDate.of(1995, 1, 1),
                "Petr Ivanov",
                new HashSet<>());

        User updatedUser = userStorage.updateUser(changedUser);

        assertEquals(1, userStorage.getUsersQuantity());

        assertThat(updatedUser)
                .isNotNull()
                .isEqualTo(changedUser);
    }

    @Test
    public void test_T0050_NS01_updateUser_checkBirthdayConstraint() {
        assertEquals(0, userStorage.getUsersQuantity());

        User user = new User(1,
                "user@email.ru",
                "vanya123",
                LocalDate.of(1990, 1, 1),
                "Ivan Petrov",
                new HashSet<>());

        int id = userStorage.addUser(user);
        assertEquals(1, userStorage.getUsersQuantity());

        User changedUser = new User(id,
                "user@email.ru",
                "IVAN_123",
                LocalDate.of(2999, 1, 1),
                "Petr Ivanov",
                new HashSet<>());

        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> userStorage.updateUser(changedUser));
    }

    @Test
    public void test_T0050_NS02_updateUser_unknownUser() {
        User user = new User(9999,
                "user@email.ru",
                "vanya123",
                LocalDate.of(1990, 1, 1),
                "",
                new HashSet<>());

        assertThrows(org.springframework.dao.EmptyResultDataAccessException.class, () -> userStorage.updateUser(user));
    }
}