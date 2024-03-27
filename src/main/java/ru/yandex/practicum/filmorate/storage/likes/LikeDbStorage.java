package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean like(int filmId, int userId) {
        log.debug("LikeDbStorage - storage.like()");

        String sqlRequest = "INSERT INTO likes (film_id, user_id)\n" +
                "VALUES (?, ?)";

        try {
            jdbcTemplate.update(sqlRequest, filmId, userId);
        } catch (DuplicateKeyException e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean unlike(int filmId, int userId) {
        log.debug("LikeDbStorage - storage.like()");

        String sqlRequest = "DELETE FROM likes WHERE film_id = ? AND user_id = ? ";
        int deleted = jdbcTemplate.update(sqlRequest, filmId, userId);

        return deleted > 0;
    }

    @Override
    public int getLikes(int filmId) {
        log.debug("LikeDbStorage - storage.getLikes()");

        String sqlRequest = "SELECT COUNT(user_id) AS rate\n" +
                "FROM likes\n" +
                "WHERE film_id = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest, filmId);

        if (rowSet.next())
            return rowSet.getInt("rate");

        return 0;
    }
    }