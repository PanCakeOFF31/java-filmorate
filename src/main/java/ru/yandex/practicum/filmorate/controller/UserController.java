package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.MethodNotImplemented;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping
    public List<User> receiveUsers(@RequestParam(defaultValue = "10") int count) {
        log.debug("/users - GET: getUsers()");
        return service.receiveUsers(count);
    }

    @GetMapping(value = "/{id}")
    public User receiveUserById(@PathVariable(name = "id") int userId) {
        return service.receiveUserById(userId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody final User user) {
        log.debug("/users - POST: createUser()");
        return service.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody final User user) {
        log.debug("/users - PUT: updateUser()");
        return service.updateUser(user);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public User addToFriend(@PathVariable(name = "id") int userId,
                            @PathVariable int friendId) {
        log.debug("/users/{}/friends/{}} - PUT: addToFriend()", userId, friendId);
        return service.addToFriend(userId, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public User deleteFromFriend(@PathVariable(name = "id") int userId,
                                 @PathVariable int friendId) {
        log.debug("/users/{id}/friends/{friendId}} - DELETE: deleteFromFriend()");
        return service.deleteFromFriend(userId, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        log.debug("/users/{id}/friends - GET: getUserFriends()");
        return service.getUserFriends(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable(name = "id") int userId,
                                       @PathVariable(name = "otherId") int otherUserId) {
        log.debug("/users/{id}/friends/common/{otherId}} - GET: getCommonFriends()");
        return service.getCommonFriends(userId, otherUserId);
    }

    // TODO: Удаление фильмов и пользователей 2 SP. Реализовать функциональность.
    @DeleteMapping(value = "/{id}")
    public Film deleteUserById(@PathVariable(name = "id") int userId) {
        log.debug("/users/{} - DELETE: deleteUserById()", userId);
        throw new MethodNotImplemented("Метод удаления пользователей по идентификатору");
    }

    // TODO: Функциональность «Лента событий». 3 SP. Реализовать функциональность
    @GetMapping("/{id}/feed")
    public List<Event> getUserFeed(@PathVariable(name = "id") final int userId) {
        log.debug("/users/{}/feed - GET: getUserFeed()", userId);
        throw new MethodNotImplemented("Метод получения списка событий пользователя");
    }

    //    TODO: Функциональность «Рекомендации». 3 SP. Реализовать функциональность
    @GetMapping("/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable(name = "id") final int userId) {
        log.debug("/users/{}/recommendations - GET: getRecommendations()", userId);
        throw new MethodNotImplemented("Метод возвращает рекомендации по фильмам для просмотра");
    }
}