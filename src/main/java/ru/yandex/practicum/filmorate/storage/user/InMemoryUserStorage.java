package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int generateId = 1;

    public int getUsersSize() {
        log.debug("(InMemoryUserStorage) storage.getUserSize()");
        return users.size();
    }

    private int generateId() {
        while (users.containsKey(generateId))
            ++generateId;

        return this.generateId;
    }

    @Override
    public int getUsersQuantity() {
        log.info("Возвращен список пользователей в количестве: " + users.size());
        return users.size();
    }

    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public Integer addUser(User user) {
        return null;
    }

    @Override
    public boolean containsUser(User user) {
        return false;
    }

    @Override
    public void udpateUser() {

    }
}
