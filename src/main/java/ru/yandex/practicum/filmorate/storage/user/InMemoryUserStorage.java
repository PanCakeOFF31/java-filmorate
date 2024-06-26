package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int generateId = 1;

    @Override
    public int getUsersQuantity() {
        log.debug("InMemoryUserStorage - users.getUsersQuantity()");
        return users.size();
    }

    @Override
    public List<User> getUsers(int count) {
        throw new NotYetImplementedException();
    }

    @Override
    public List<User> getUsers() {
        log.debug("InMemoryUserStorage - users.getUsers()");
        return new ArrayList<>(users.values());
    }

    @Override
    public Set<Integer> getAllRowId() {
        log.debug("InMemoryUserStorage - users.getKeys()");
        return new HashSet<>(users.keySet());
    }

    @Override
    public Integer addUser(User user) {
        log.debug("InMemoryUserStorage - users.addUser()");
        Integer generatedId = generateId();

        user.setId(generatedId);
        users.put(generatedId, user);

        return generatedId;
    }

    @Override
    public User getUserById(int id) {
        log.debug("InMemoryUserStorage - users.getUserById()");
        return users.get(id);
    }

    @Override
    public boolean containsById(int id) {
        log.debug("InMemoryUserStorage - users.containsById().");
        return users.containsKey(id);
    }

    @Override
    public User updateUser(User user) {
        log.debug("InMemoryUserStorage - users.updateUser()");

        Integer key = user.getId();
        return users.put(key, user);
    }

    @Override
    public User deleteUserById(int id) {
        log.debug("InMemoryUserStorage - users.deleteUser().");
        User user = users.get(id);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException();
        }
    }

    private int generateId() {
        log.debug("InMemoryUserStorage - users.generatedId()");

        while (users.containsKey(generateId))
            ++generateId;

        return this.generateId;
    }
}