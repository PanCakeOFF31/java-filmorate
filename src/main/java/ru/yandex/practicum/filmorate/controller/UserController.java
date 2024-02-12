package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> receiveUsers(@RequestParam(defaultValue = "10") int count) {
        log.debug("/users - GET: getUsers()");
        return service.receiveUsers(count);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody final User user) {
        log.debug("/users - POST: createUser()");
        return service.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody final User user) {
        log.debug("/users - PUT: updateUser()");
//        return service.updateUser(user);
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Метод /feed ещё не реализован.");
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addToFriend(@PathVariable(name = "id") int userId,
                            @PathVariable int friendId) {
        log.debug("/users/{id}/friends/{friendId}} - PUT: addToFriend()");
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Метод /feed ещё не реализован.");

    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void deleteFromFriend(@PathVariable(name = "id") int userId,
                                 @PathVariable int friendId) {
        log.debug("/users/{id}/friends/{friendId}} - DELETE: deleteFromFriend()");
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Метод /feed ещё не реализован.");
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        log.debug("/users/{id}/friends - GET: getUserFriends()");
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Метод /feed ещё не реализован.");
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable(name = "id") int userID,
                                       @PathVariable(name = "otherId") int otherUserId) {
        log.debug("/users/{id}/friends/common/{otherId}} - GET: getCommonFriends()");
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Метод /feed ещё не реализован.");
    }
}























