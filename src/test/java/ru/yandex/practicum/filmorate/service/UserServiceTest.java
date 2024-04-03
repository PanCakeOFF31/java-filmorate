package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.user.SameUserException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserNullValueValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@Sql(scripts = {"file:./src/test/resources/test-schema.sql",
        "file:./src/test/resources/test-data.sql"}, executionPhase = BEFORE_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {
    private final UserService userService;
    private final UserStorage userStorage;
    private User user;

    @Test
    public void test_T0010_PS01_updateValidation() {
        user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));

        user = userService.createUser(user);
        assertNotNull(user.getId());

        boolean actual = userService.updateValidation(user);
        assertTrue(actual);

        assertEquals(1, userStorage.getUsersQuantity());
        userService.updateUser(user);
        assertEquals(1, userStorage.getUsersQuantity());
    }

    @Test
    public void test_T0010_NS01_updateValidation_noId() {
        User user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));

        assertNull(user.getId());

        assertEquals(0, userStorage.getUsersQuantity());
        assertThrows(UserNullValueValidationException.class, () -> userService.updateUser(user));
        assertEquals(0, userStorage.getUsersQuantity());
    }

    @Test
    public void test_T0010_NS02_updateValidation_incorrectId() {
        User user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));

        user.setId(9999);

        assertEquals(0, userStorage.getUsersQuantity());
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(user));
        assertEquals(0, userStorage.getUsersQuantity());
    }

    @Test
    public void test_T0020_PS01_coupleUserValidation() {
        assertEquals(0, userStorage.getUsersQuantity());

        User user1 = new User();

        user1.setLogin("dolore");
        user1.setName("");
        user1.setEmail("mail@mail.ru");
        user1.setBirthday(LocalDate.of(1946, 8, 20));
        user1.setFriends(new HashSet<>());

        user1 = userService.createUser(user1);

        User user2 = new User(user1);
        user2.setLogin("dolore-2");
        user2.setEmail("dolore-2@mail.ru");
        user2 = userService.createUser(user2);

        Integer userId1 = user1.getId();
        Integer userId2 = user2.getId();

        assertNotNull(userId1);
        assertNotNull(userId2);

        assertNotEquals(userId1, userId2);
        assertEquals(2, userStorage.getUsersQuantity());
    }

    @Test
    public void test_T0020_NS01_coupleUserValidation_sameId() {
        assertThrows(SameUserException.class, () -> userService.coupleUserValidation(1, 1));
    }

    @Test
    public void test_T0020_NS02_coupleUserValidation_noContainsUserId() {
        assertEquals(0, userStorage.getUsersQuantity());

        user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        user.setFriends(new HashSet<>());

        user = userService.createUser(user);
        Integer userId = user.getId();

        assertNotNull(userId);
        assertThrows(UserNotFoundException.class, () -> userService.coupleUserValidation(userId + 1, userId));
    }

    @Test
    public void test_T0020_NS03_coupleUserValidation_notContainsOtherUserId() {
        assertEquals(0, userStorage.getUsersQuantity());

        user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        user.setFriends(new HashSet<>());

        user = userService.createUser(user);
        Integer userId = user.getId();

        assertNotNull(userId);
        assertThrows(UserNotFoundException.class, () -> userService.coupleUserValidation(userId, userId + 1));
    }
}