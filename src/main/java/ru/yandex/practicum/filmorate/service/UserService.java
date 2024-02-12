package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.user.UserNullValueValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public List<User> receiveUsers(int count) {
        log.debug("UserService - service.receiveUsers()");

        if (count < 0) {
            log.info("Возвращен список пользователей в количестве: " + storage.getUsersQuantity());
            return new ArrayList<>(storage.getUsers());
        }

        if (count > storage.getUsersQuantity())
            count = storage.getUsersQuantity();

        List<User> users = new ArrayList<>(storage.getUsers());
        Collections.shuffle(users);

        log.info("Возвращен список пользователей в количестве: " + count);

        return users.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public User createUser(final User user) {
        log.debug("UserService service.createUser()");

        Integer id = storage.addUser(user);
        user.setFriends(new HashSet<>());
        correctName(user);

        log.info("Пользователь добавлен с Id: " + id);
        log.info(user.toString());
        log.info("Количество пользователей: " + storage.getUsersQuantity());

        return user;
    }

    public User updateUser(final User user) {
        log.debug("UserService service.updateUser()");
        updateValidation(user);

        storage.udpateUser(user);

        log.info("Пользователь обновлен: " + user);
        log.info("Количество пользователей: " + storage.getUsersQuantity());

        return user;
    }


    public boolean updateValidation(final User user) {
        log.debug("Начало updateValidation() валидации пользователя: " + user);

        if (user.getId() == null) {
            String message = "У пользователя нет id, валидация не пройдена: " + user;
            log.warn(message);
            throw new UserNullValueValidationException(message);
        }

        if (!storage.containsUser(user)) {
            String message = "Валидация на существование пользователя по id не пройдена: " + user;
            log.warn(message);
            throw new UserNullValueValidationException(message);
        }

        log.info("Успешное окончание updateValidation() валидации пользователя: " + user);
        return true;
    }

    private void correctName(final User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя скорректировано и указано в качестве логина");
        }
    }

}
