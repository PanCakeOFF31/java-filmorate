package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void like(int userId, int filmId) {

    }

    @Override
    public void unlike(int userId, int filmId) {

    }

    @Override
    public Set<Integer> getLikes(int filmId) {
        return null;
    }

    @Override
    public Set<User> getLikesAsUsers(int filmId) {
        return null;
    }
}