package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserConttroller {
    private final Map<Integer, User> users = new HashMap<>();
    private int generateId = 1;

    @GetMapping
    public List<User> getUsers() {
        log.debug("/users - GET: getUsers()");
        log.info("Возвращен список пользователей в количестве: " + users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody final User user) {
        log.debug("/users - POST: createUser()");

        user.setId(generateId());
        users.put(generateId, user);

        log.info("Пользователь добавлен: " + user);
        log.info("Количество пользователей: " + users.size());

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody final User user) {
        log.debug("/users - PUT: updateUser()");

        if (!updateValidation(user)) {
            log.warn("Пользователь не обновлен, так как не прошел валидацию");
            throw new ValidationException();
        }

        users.put(user.getId(), user);

        log.info("Пользователь обновлен: " + user);
        log.info("Количество пользователей: " + users.size());

        return user;
    }

    public boolean updateValidation(final User user) {
        log.debug("Начало updateValidation() валидации пользователя: " + user);

        if (user.getId() == null) {
            log.warn("У пользователя нет id, валидация не пройдена: " + user);
            return false;
        }

        if (users.containsKey(user.getId())) {
            log.info("Успешное окончание updateValidation() валидации пользователя: " + user);
            return true;
        } else {
            log.warn("Валидация на существование пользователя по id не пройдена: " + user);
            return false;
        }
    }

    private int generateId() {
        while (users.containsKey(generateId))
            ++generateId;

        return this.generateId;
    }


}
