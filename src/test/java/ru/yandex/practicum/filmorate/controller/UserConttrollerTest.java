package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserConttrollerTest {

    private UserConttroller userConttroller;
    private User user;

    @BeforeEach
    private void initialize() {
        userConttroller = new UserConttroller();
    }

    @Test
    public void T0010_PS01_updateValidation() {
        user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));

        userConttroller.createUser(user);
        assertEquals(1, user.getId());

        boolean actual = userConttroller.updateValidation(user);
        assertTrue(actual);
    }

    @Test
    public void T0010_NS01_updateValidation_noId() {
        User user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));

        assertNull(user.getId());
        assertThrows(ValidationException.class, () -> userConttroller.updateUser(user));

        boolean actual = userConttroller.updateValidation(user);
        assertFalse(actual);
    }

    @Test
    public void T0010_NS02_updateValidation_incorrectId() {
        User user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));

        user.setId(9999);
        assertThrows(ValidationException.class, () -> userConttroller.updateUser(user));

        boolean actual = userConttroller.updateValidation(user);
        assertFalse(actual);
    }
}