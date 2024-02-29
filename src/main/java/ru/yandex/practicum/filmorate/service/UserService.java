package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.user.SameUserException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserNullValueValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipDao;

    public List<User> receiveUsers(int count) {
        log.debug("UserService - service.receiveUsers()");
        int usersQuantity = userStorage.getUsersQuantity();

        if (count < 0) {
            log.info("Возвращен список пользователей в количестве: " + usersQuantity);
            return new ArrayList<>(userStorage.getUsers());
        }

        if (count > usersQuantity)
            count = usersQuantity;

        List<User> users = new ArrayList<>(userStorage.getUsers(count));

        log.info("Возвращен список пользователей в количестве: " + count);

        return users;
    }

    public User receiveUserById(int userId) {
        String message = "Пользователя нет с id :" + userId;
        userIsExist(userId, message);
        return userStorage.getUserById(userId);
    }

    public User createUser(final User user) {
        log.debug("UserService - service.createUser()");

        correctName(user);
        user.setFriends(new HashSet<>());
        Integer id = userStorage.addUser(user);

        log.info("Пользователь добавлен с Id: " + id);
        User addedUser = userStorage.getUserById(id);
        log.info(addedUser.toString());
        log.info("Количество пользователей: " + userStorage.getUsersQuantity());

        return addedUser;
    }

    public User updateUser(final User user) {
        log.debug("UserService - service.updateUser()");
        updateValidation(user);
        correctName(user);

        User updatedUser = userStorage.updateUser(user);

        log.info("Пользователь обновлен: " + updatedUser);
        log.info("Количество пользователей: " + userStorage.getUsersQuantity());

        return updatedUser;
    }

    public User addToFriend(int userId, int friendId) {
        log.debug("UserService - service.addToFriend()");
        coupleUserValidation(userId, friendId);

        log.info("Количеств друзей изначально:" + friendshipDao.getUserFriendCounts(userId));
        boolean result = friendshipDao.addToFriend(userId, friendId);

        String message = result
                ? "Пользователь с " + userId + " добавил в друзья пользователя " + friendId
                : ("Пользователь уже добавлен в друзья");

        log.info(message);
        log.info("Количество друзей теперь: " + friendshipDao.getUserFriendCounts(userId));

        return userStorage.getUserById(userId);
    }

    public User deleteFromFriend(int userId, int friendId) {
        log.debug("UserService - service.deleteFromFriend()");

        coupleUserValidation(userId, friendId);

        User user = userStorage.getUserById(userId);
        log.info("Количеств лайка в изначально:" + user.friendsQuantity());
        boolean result = user.unfriend(friendId);

        String message = result
                ? ("Пользователь с " + userId + " удалил из друзей пользователя" + friendId)
                : ("Пользователь уже удален из друзей");

        log.info(message);
        log.info("Количество друзей теперь: " + user.friendsQuantity());

        friendshipDao.deleteFromFriend(userId, friendId);

        return user;
    }

    public List<User> getUserFriends(int userId) {
        log.debug("UserService - service.getUserFriend()");

        String message = "Пользователя нет с id :" + userId;
        userIsExist(userId, message);

        List<User> userFriends = friendshipDao.getUserFriendsAsUsers(userId);

        log.info("Для пользователя " + userId + " возвращены друзья в количестве " + userFriends.size());
        return userFriends;
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        log.debug("UserService - service.getCommonFriends()");

        coupleUserValidation(userId, otherUserId);

        List<User> commonFriends = friendshipDao.getCommonFriendsAsUsers(userId, otherUserId);

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

        String message = "Валидация на существование пользователя по id не пройдена: " + user;
        userIsExist(user.getId(), message);

        log.info("Успешное окончание updateValidation() валидации пользователя: " + user);
        return true;
    }

    public boolean coupleUserValidation(int userId, int otherUserId) {
        log.debug("UserService - service.toFriendValidation()");
        String message;

        if (userId == otherUserId) {
            message = "Идентификаторы пользователей совпадают: " + userId + "==" + otherUserId;
            log.warn(message);
            throw new SameUserException(message);
        }

        message = "Пользователя нет с id :" + userId;
        userIsExist(userId, message);

        message = "Второго пользователя нет с id :" + otherUserId;
        userIsExist(otherUserId, message);

        log.info("Успешное окончание coupleUserValidation() валидации");
        return true;
    }

    private boolean userIsExist(int userId, String message) {
        if (!userStorage.containsById(userId)) {
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