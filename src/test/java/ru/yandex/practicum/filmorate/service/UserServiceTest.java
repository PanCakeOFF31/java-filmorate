package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.user.SameUserException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserNullValueValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService service;
    private UserStorage storage;
    private User user;

    @BeforeEach
    public void initialize() {
        storage = new InMemoryUserStorage();
        service = new UserService(storage);
    }

    @Test
    public void test_T0010_PS01_updateValidation() {
        user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));

        service.createUser(user);
        assertNotNull(user.getId());

        boolean actual = service.updateValidation(user);
        assertTrue(actual);

        assertEquals(1, storage.getUsersQuantity());
        service.updateUser(user);
        assertEquals(1, storage.getUsersQuantity());
    }

    @Test
    public void test_T0010_NS01_updateValidation_noId() {
        User user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));

        assertNull(user.getId());

        assertEquals(0, storage.getUsersQuantity());
        assertThrows(UserNullValueValidationException.class, () -> service.updateUser(user));
        assertEquals(0, storage.getUsersQuantity());
    }

    @Test
    public void test_T0010_NS02_updateValidation_incorrectId() {
        User user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));

        user.setId(9999);

        assertEquals(0, storage.getUsersQuantity());
        assertThrows(UserNotFoundException.class, () -> service.updateUser(user));
        assertEquals(0, storage.getUsersQuantity());
    }

    @Test
    public void test_T0020_PS01_coupleUserValidation() {
        assertEquals(0, storage.getUsersQuantity());

        User user1 = new User();

        {
            user1.setLogin("dolore");
            user1.setName("");
            user1.setEmail("mail@mail.ru");
            user1.setBirthday(LocalDate.of(1946, 8, 20));
            user1.setFriends(new HashSet<>());
        }

        service.createUser(user1);
        assertNotNull(user1.getId());

        User user2 = new User(user1);
        service.createUser(user2);
        assertNotNull(user2.getId());

        Integer userId1 = user1.getId();
        Integer userId2 = user2.getId();

        assertNotEquals(userId1, userId2);
        assertEquals(2, storage.getUsersQuantity());
    }

    @Test
    public void test_T0020_NS01_coupleUserValidation_sameId() {
        assertThrows(SameUserException.class, () -> service.coupleUserValidation(1, 1));
    }

    @Test
    public void test_T0020_NS02_coupleUserValidation_noContainsUserId() {
        assertEquals(0, storage.getUsersQuantity());

        user = new User();

        {
            user.setLogin("dolore");
            user.setName("");
            user.setEmail("mail@mail.ru");
            user.setBirthday(LocalDate.of(1946, 8, 20));
            user.setFriends(new HashSet<>());
        }

        service.createUser(user);
        assertNotNull(user.getId());

        Integer userId = user.getId();
        assertThrows(UserNotFoundException.class, () -> service.coupleUserValidation(userId + 1, userId));
    }

    @Test
    public void test_T0020_NS03_coupleUserValidation_notContainsOtherUserId() {
        assertEquals(0, storage.getUsersQuantity());

        user = new User();

        {
            user.setLogin("dolore");
            user.setName("");
            user.setEmail("mail@mail.ru");
            user.setBirthday(LocalDate.of(1946, 8, 20));
            user.setFriends(new HashSet<>());
        }

        service.createUser(user);
        assertNotNull(user.getId());

        Integer userId = user.getId();
        assertThrows(UserNotFoundException.class, () -> service.coupleUserValidation(userId, userId + 1));
    }
}