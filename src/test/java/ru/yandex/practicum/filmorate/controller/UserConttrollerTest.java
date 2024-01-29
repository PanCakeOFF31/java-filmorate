package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserConttrollerTest {

    private UserController userConttroller;
    private User user;

    @BeforeEach
    public void initialize() {
        userConttroller = new UserController();
    }

    @Test
    public void test_T0010_PS01_updateValidation() {
        user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));

        userConttroller.createUser(user);
        assertNotNull(user.getId());

        boolean actual = userConttroller.updateValidation(user);
        assertTrue(actual);

        assertEquals(1, userConttroller.getUsers().size());
        userConttroller.updateUser(user);
        assertEquals(1, userConttroller.getUsers().size());
    }

    @Test
    public void test_T0010_NS01_updateValidation_noId() {
        User user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));

        assertNull(user.getId());
        boolean actual = userConttroller.updateValidation(user);
        assertFalse(actual);

        assertEquals(0, userConttroller.getUsers().size());
        assertThrows(ValidationException.class, () -> userConttroller.updateUser(user));
        assertEquals(0, userConttroller.getUsers().size());
    }

    @Test
    public void test_T0010_NS02_updateValidation_incorrectId() {
        User user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));

        user.setId(9999);
        boolean actual = userConttroller.updateValidation(user);
        assertFalse(actual);

        assertEquals(0, userConttroller.getUsers().size());
        assertThrows(ValidationException.class, () -> userConttroller.updateUser(user));
        assertEquals(0, userConttroller.getUsers().size());
    }
}