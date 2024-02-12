package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage {
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
}
