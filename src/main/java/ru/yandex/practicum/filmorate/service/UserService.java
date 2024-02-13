package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.user.SameUserException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserNullValueValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> receiveUsers(int count) {
        log.debug("UserService - service.receiveUsers()");

        if (count < 0) {
            log.info("Возвращен список пользователей в количестве: " + userStorage.getUsersQuantity());
            return new ArrayList<>(userStorage.getUsers());
        }

        if (count > userStorage.getUsersQuantity())
            count = userStorage.getUsersQuantity();

        List<User> users = new ArrayList<>(userStorage.getUsers());
        Collections.shuffle(users);

        log.info("Возвращен список пользователей в количестве: " + count);

        return users.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public User receiveUserById(int userId) {
        if (!userStorage.containsById(userId)) {
            String message = "Пользователя нет с id :" + userId;
            log.warn(message);
            throw new UserNotFoundException(message);
        }

        return userStorage.getUserById(userId);
    }

    public User createUser(final User user) {
        log.debug("UserService - service.createUser()");

        Integer id = userStorage.addUser(user);
        user.setFriends(new HashSet<>());
        correctName(user);

        log.info("Пользователь добавлен с Id: " + id);
        log.info(user.toString());
        log.info("Количество пользователей: " + userStorage.getUsersQuantity());

        return user;
    }

    public User updateUser(final User user) {
        log.debug("UserService - service.updateUser()");
        updateValidation(user);

        Set<Integer> friends = userStorage.getUserById(user.getId()).getFriends();
        user.setFriends(friends);

        userStorage.updateUser(user);

        log.info("Пользователь обновлен: " + user);
        log.info("Количество пользователей: " + userStorage.getUsersQuantity());

        return user;
    }

    public User addToFriend(int userId, int friendId) {
        log.debug("UserService - service.addToFriend()");

        coupleUserValidation(userId, friendId);

        User user = userStorage.getUserById(userId);
        log.info("Количеств друзей изначально:" + user.friendsQuantity());
        boolean result = user.toFriend(friendId);

        if (result)
            log.info("Пользователь с " + userId + " добавил в друзья пользователя " + friendId);
        else
            log.info("Пользователь уже добавлен в друзья");

        log.info("Количество друзей теперь: " + user.friendsQuantity());

        userStorage.getUserById(friendId).toFriend(userId);

        return user;
    }

    public User deleteFromFriend(int userId, int friendId) {
        log.debug("UserService - service.deleteFromFriend()");

        coupleUserValidation(userId, friendId);

        User user = userStorage.getUserById(userId);
        log.info("Количеств лайка в изначально:" + user.friendsQuantity());
        boolean result = user.unfriend(friendId);

        if (result)
            log.info("Пользователь с " + userId + " удалил из друзей пользователя" + friendId);
        else
            log.info("Пользователь уже удален из друзей");

        log.info("Количество друзей теперь: " + user.friendsQuantity());

        userStorage.getUserById(friendId).unfriend(userId);

        return user;
    }

    public List<User> getUserFriends(int userId) {
        log.debug("UserService - service.getUserFriend()");

        if (!userStorage.containsById(userId)) {
            String message = "Пользователя нет с id :" + userId;
            log.warn(message);
            throw new UserNotFoundException(message);
        }

        User user = userStorage.getUserById(userId);
        List<User> userFriends = user.getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());

        log.info("Для пользователя " + userId + " возвращены друзья в количестве " + userFriends.size());
        return userFriends;
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        log.debug("UserService - service.getCommonFriends()");

        coupleUserValidation(userId, otherUserId);

        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherUserId);

        Set<Integer> otherUserFriends = otherUser.getFriends();
        List<User> commonFriends =
                user.getFriends().stream()
                        .filter(otherUserFriends::contains)
                        .map(userStorage::getUserById)
                        .collect(Collectors.toList());

        log.info("Для пользователя " + userId + " возвращены общие друзья с пользователем " + otherUserId);
        log.info("Количество общих друзей: " + commonFriends.size());

        return commonFriends;
    }


    public boolean updateValidation(final User user) {
        log.debug("UserService - service.updateValidation()");
        log.debug("Валидации пользователя: " + user);

        if (user.getId() == null) {
            String message = "У пользователя нет id, валидация не пройдена: " + user;
            log.warn(message);
            throw new UserNullValueValidationException(message);
        }

        if (!userStorage.containsUser(user)) {
            String message = "Валидация на существование пользователя по id не пройдена: " + user;
            log.warn(message);
            throw new UserNotFoundException(message);
        }

        log.info("Успешное окончание updateValidation() валидации пользователя: " + user);
        return true;
    }

    public boolean coupleUserValidation(int userId, int otherUserId) {
        log.debug("UserService - service.toFriendValidation()");

        if (userId == otherUserId) {
            String message = "Идентификаторы пользователей совпадают: " + userId + "==" + otherUserId;
            log.warn(message);
            throw new SameUserException(message);
        }

        if (!userStorage.containsById(userId)) {
            String message = "Пользователя нет с id :" + userId;
            log.warn(message);
            throw new UserNotFoundException(message);
        }

        if (!userStorage.containsById(otherUserId)) {
            String message = "Второго пользователя нет с id :" + otherUserId;
            log.warn(message);
            throw new UserNotFoundException(message);
        }


        return true;
    }

    private void correctName(final User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя скорректировано и указано в качестве логина");
        }
    }

}
