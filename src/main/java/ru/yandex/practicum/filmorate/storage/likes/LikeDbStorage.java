package ru.yandex.practicum.filmorate.storage.likes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Component
@Slf4j
public class LikeDbStorage implements LikeStorage {
    @Override
    public Set<Integer> getLikes(int filmId) {
        return null;
    }

    @Override
    public Set<User> getUserLikes(int filmId) {
        return null;
    }
}
