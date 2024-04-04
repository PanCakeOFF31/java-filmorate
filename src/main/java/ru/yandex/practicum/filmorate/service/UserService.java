package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.user.SameUserException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserNullValueValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipDao;
    private final ExistChecker existChecker;
    private final LikeStorage likeStorage;
    private final FilmStorage filmStorage;

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
        existChecker.userIsExist(userId);

        return userStorage.getUserById(userId);
    }

    public User createUser(final User user) {
        log.debug("UserService - service.createUser()");

        correctName(user);
        correctFriends(user);

        Integer id = userStorage.addUser(user);

        log.info("Пользователь добавлен с Id: " + id);
        User addedUser = userStorage.getUserById(id);
        log.info(addedUser.toString());
        log.info("Количество пользователей: " + userStorage.getUsersQuantity());

        return addedUser;
    }

    public User updateUser(final User user) {
        log.debug("UserService - service.updateUser()");

        correctFriends(user);
        correctName(user);
        updateValidation(user);

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

        log.info("Количеств друзей изначально: " + friendshipDao.getUserFriendCounts(userId));
        boolean result = friendshipDao.deleteFromFriend(userId, friendId);

        String message = result
                ? ("Пользователь с " + userId + " удалил из друзей пользователя" + friendId)
                : ("Пользователь уже удален из друзей");

        log.info(message);
        log.info("Количество друзей теперь: " + friendshipDao.getUserFriendCounts(userId));

        return userStorage.getUserById(userId);
    }

    public List<User> getUserFriends(int userId) {
        log.debug("UserService - service.getUserFriend()");

        existChecker.userIsExist(userId);

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
        existChecker.userIsExist(user.getId(), message);

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
        existChecker.userIsExist(userId, message);

        message = "Второго пользователя нет с id :" + otherUserId;
        existChecker.userIsExist(otherUserId, message);

        log.info("Успешное окончание coupleUserValidation() валидации");
        return true;
    }

    private void correctName(final User user) {
        log.debug("UserService - service.correctName()");

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя скорректировано и указано в качестве логина");
        }

        log.info("У пользователя есть имя, корректировать не понадобилось");
    }

    private void correctFriends(final User user) {
        log.debug("UserService - service.correctFriends()");

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
            log.info("Друзья инициализированы пустым множеством");
        }

        log.info("У пользователя указаны друзья, коррекции не было");
    }

    public List<Film> getRecommendations(final int userId) {
        log.debug("UserService - service.getRecommendations()");

        User user = userStorage.getUserById(userId);

        if (user == null) {
            throw new UserNotFoundException("Такого пользователя с id = " + userId + " не существует в хранилище");
        }



        // составляем матрицу (на основании всех лайков) Id пользователя - список Id понравившихся фильмов
        Map<Integer, Set<Integer>> usersFavoriteFilms = likeStorage.getUsersFavoriteFilms();

        // поиск пользователей с максимальным количеством пересечения по лайкам
        List<Integer> usersIdWithSameTastes = getUsersIdWithSameTastes(userId, usersFavoriteFilms);
        if (usersIdWithSameTastes.isEmpty()) {
            return new ArrayList<>();
        }

        // определение фильмов, которые не пролайкал пользователь, которому надо дать рекомендацию,
        // с весом популярности среди пользователей со схожими вкусами
        Map<Integer, Integer> newFilmsRating = getFilmsWithAuthority(userId, usersFavoriteFilms, usersIdWithSameTastes);
        if (newFilmsRating.isEmpty()) {
            return new ArrayList<>();
        }
        Integer maxAuthority = newFilmsRating.values().stream().max(Comparator.comparingInt((Integer val) -> val)).get();

        List<Integer> filmRecommendedIds = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry: newFilmsRating.entrySet()) {
            if (entry.getValue().equals(maxAuthority)){
                filmRecommendedIds.add(entry.getKey());
            }
        }

        return filmStorage.getSelectedFilms(filmRecommendedIds);
    }

    private Map<Integer, Integer> getFilmsWithAuthority(int userId, Map<Integer, Set<Integer>> usersFavoriteFilms, List<Integer> usersIdWithSameTastes) {
        Map<Integer, Integer> newFilmsRating = new HashMap<>();

        // собираем список со всеми фильмами пользователей со схожими вкусами
        List<Integer> films = new ArrayList<>();
        for (Integer otherUserId: usersIdWithSameTastes) {
            films.addAll(usersFavoriteFilms.get(otherUserId));
        }

        // удаляем фильмы, которые лайкнул основной пользователь
        films.removeAll(usersFavoriteFilms.get(userId));

        // собираем табличку Id фильма-вес
        for (Integer filmId: films) {
            if(!newFilmsRating.containsKey(filmId)) {
                newFilmsRating.put(filmId, 1);
            } else {
                newFilmsRating.put(filmId, newFilmsRating.get(filmId) + 1);
            }
        }

        return newFilmsRating;
    }

    private List<Integer> getUsersIdWithSameTastes(Integer userId, Map<Integer, Set<Integer>> usersFavoriteFilms) {
        // поиск пользователей с максимальным количеством пересечения по лайкам
        List<Integer> usersIdWithSameTastes = new ArrayList<>();
        int maxSameCount = 0;

        if (!usersFavoriteFilms.isEmpty() && usersFavoriteFilms.containsKey(userId)) {
            Set<Integer> userFilms = usersFavoriteFilms.get(userId);
            for (Map.Entry<Integer, Set<Integer>> entry: usersFavoriteFilms.entrySet()) {
                if (!entry.getKey().equals(userId)) {
                    // считаем кол-во совпадений по лайкнутым фильмам
                    int sameCount = 0;
                    for (Integer filmId: userFilms) {
                        if (entry.getValue().contains(filmId)){
                            sameCount++;
                        }
                    }

                    if (sameCount >= maxSameCount) {
                        if (sameCount > maxSameCount) {
                            maxSameCount = sameCount;
                            usersIdWithSameTastes.clear();
                        }
                        usersIdWithSameTastes.add(entry.getKey());
                    }
                }
            }
        }

        return usersIdWithSameTastes;
    }

}