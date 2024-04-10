package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean like(int filmId, int userId) {
        log.debug("LikeDbStorage - storage.like()");

        String sqlRequest = "INSERT INTO film_like (film_id, user_id)\n" +
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

        String sqlRequest = "DELETE FROM film_like WHERE film_id = ? AND user_id = ? ";
        int deleted = jdbcTemplate.update(sqlRequest, filmId, userId);

        return deleted > 0;
    }

    @Override
    public int getLikes(int filmId) {
        log.debug("LikeDbStorage - storage.getLikes()");

        String sqlRequest = "SELECT COUNT(user_id) AS rate\n" +
                "FROM film_like\n" +
                "WHERE film_id = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest, filmId);

        if (rowSet.next())
            return rowSet.getInt("rate");

        return 0;
    }

    @Override
    public Map<Integer, Set<Integer>> getUsersFavoriteFilms() {
        Map<Integer, Set<Integer>> usersFavoriteFilms = new HashMap<>();

        SqlRowSet sqlRequest = jdbcTemplate.queryForRowSet("select * from film_like");
        while (sqlRequest.next()) {
            Set<Integer> likes = new HashSet<>();
            Integer userId = sqlRequest.getInt("user_id");
            Integer filmId = sqlRequest.getInt("film_id");
            if (usersFavoriteFilms.containsKey(userId)) {
                likes = usersFavoriteFilms.get(userId);
            }
            likes.add(filmId);
            usersFavoriteFilms.put(userId, likes);
        }

        return usersFavoriteFilms;
    }
}