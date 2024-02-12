package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserConttrollerTest {

    private UserController userController;
    private UserService service;
    private UserStorage storage;
    private User user;

    @BeforeEach
    public void initialize() {
        storage = new InMemoryUserStorage();
        service = new UserService(storage);
        userController = new UserController(service);
    }

    @Test
    public void test_T0010_PS01_updateValidation() {
        user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));

        userController.createUser(user);
        assertNotNull(user.getId());

        boolean actual = service.updateValidation(user);
        assertTrue(actual);

        assertEquals(1, storage.getUsersQuantity());
        userController.updateUser(user);
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
        boolean actual = service.updateValidation(user);
        assertFalse(actual);

        assertEquals(0, storage.getUsersQuantity());
        assertThrows(ValidationException.class, () -> userController.updateUser(user));
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
        boolean actual = service.updateValidation(user);
        assertFalse(actual);

        assertEquals(0, storage.getUsersQuantity());
        assertThrows(ValidationException.class, () -> userController.updateUser(user));
        assertEquals(0, storage.getUsersQuantity());
    }
}