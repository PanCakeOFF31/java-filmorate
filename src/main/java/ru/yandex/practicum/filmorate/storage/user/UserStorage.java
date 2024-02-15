package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    int getUsersQuantity();

    List<User> getUsers();

    Set<Integer> getKeys();

    User getUserById(int id);

    Integer addUser(User user);

    boolean containsUser(User user);

    boolean containsById(int id);

    User updateUser(User user);

}
