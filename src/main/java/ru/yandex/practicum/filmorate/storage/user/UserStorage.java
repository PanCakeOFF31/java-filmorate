package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    int getUsersQuantity();
    List<User> getUsers();
    Integer addUser(User user);
    boolean containsUser(User user);
    void udpateUser();
}
