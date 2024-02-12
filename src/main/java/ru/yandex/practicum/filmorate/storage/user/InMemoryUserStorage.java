package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int generateId = 1;

    public int getUsersSize() {
        log.debug("InMemoryUserStorage - users.getUserSize()");
        return users.size();
    }

    private int generateId() {
        log.debug("InMemoryUserStorage - users.generatedId()");
        while (users.containsKey(generateId))
            ++generateId;

        return this.generateId;
    }

    @Override
    public int getUsersQuantity() {
        log.debug("InMemoryUserStorage - users.getUsersQuantity()");
        return users.size();
    }

    @Override
    public List<User> getUsers() {
        log.debug("InMemoryUserStorage - users.getUsers()");
        return new ArrayList<>(users.values());
    }

    @Override
    public Integer addUser(User user) {
        log.debug("InMemoryUserStorage - users.addUser()");
        Integer generatedId = generateId();

        user.setId(generatedId);
        users.put(generatedId, user);

        log.info("Пользователь добавлен: " + user);
        return generatedId;
    }

    @Override
    public boolean containsUser(User user) {
        log.debug("InMemoryUserStorage - users.containsUser()");
        return users.containsKey(user.getId());
    }

    @Override
    public void udpateUser(User user) {
        log.debug("InMemoryUserStorage - users.updateUser()");

        Integer key = user.getId();
        User updated = users.put(key, user);

        if (updated == null)
            throw new RuntimeException("");
    }
}
