package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
        return service.updateUser(user);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public User addToFriend(@PathVariable(name = "id") int userId,
                            @PathVariable int friendId) {
        log.debug("/users/{id}/friends/{friendId}} - PUT: addToFriend()");
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
    public List<User> getCommonFriends(@PathVariable(name = "id") int userID,
                                       @PathVariable(name = "otherId") int otherUserId) {
        log.debug("/users/{id}/friends/common/{otherId}} - GET: getCommonFriends()");
        return service.getCommonFriends(userID, otherUserId);
    }
}























