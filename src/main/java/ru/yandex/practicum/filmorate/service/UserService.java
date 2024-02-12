package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public List<User> receiveUsers() {
        log.debug("(UserService) service.receiveUsers()");
        log.info("Возвращен список пользователей в количестве: " + users.size());
        return null;
    }

    public User createUser(final User user) {
        log.debug("(UserService) service.createUser()");

        user.setId(generateId());
        users.put(generateId, user);

        correctName(user);

        log.info("Пользователь добавлен: " + user);
        log.info("Количество пользователей: " + users.size());

        return user;
    }

    public User updateUser(final User user) {
        log.debug("(UserService) service.updateUser()");

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

        if (!users.containsKey(user.getId())) {
            log.warn("Валидация на существование пользователя по id не пройдена: " + user);
            return false;
        }

        log.info("Успешное окончание updateValidation() валидации пользователя: " + user);

        return true;
    }

    private void correctName(final User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя указано в качестве логина");
        }
    }

}
