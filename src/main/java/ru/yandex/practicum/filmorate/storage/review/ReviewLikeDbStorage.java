package ru.yandex.practicum.filmorate.storage.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewLikeDbStorage implements ReviewLikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public int getUseful(final int reviewId) {
        log.debug("ReviewLikeDbStorage - getUseful()");

        String sqlRequest = "SELECT review_like.is_like\n" +
                "FROM review_like\n" +
                "JOIN film_review ON review_like.review_id = film_review.id\n" +
                "WHERE review_id = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest, reviewId);

        List<Boolean> useful = new ArrayList<>();
        int likeCount = 0;
        boolean isLike;

        while (rowSet.next()) {
            isLike = rowSet.getBoolean("is_like");
            useful.add(isLike);

            if (isLike)
                likeCount++;
        }

        int dislikeCount = useful.size() - likeCount;

        return likeCount - dislikeCount;
    }

    @Override
    public boolean like(final int reviewId, final int userId) {
        log.debug("ReviewLikeDbStorage - like()");

        String sqlRequest = "INSERT INTO review_like (review_id, user_id, is_like) VALUES (?, ?, true);";

        try {
            jdbcTemplate.update(sqlRequest, reviewId, userId);
        } catch (DuplicateKeyException e) {
            sqlRequest = "SELECT is_like FROM review_like WHERE review_id = ? AND user_id = ?;";

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest, reviewId, userId);
            rowSet.next();

            boolean isLike = rowSet.getBoolean("is_like");

            if (!isLike) {
                sqlRequest = "UPDATE review_like SET is_like = true WHERE review_id = ? AND user_id = ?";
                jdbcTemplate.update(sqlRequest, reviewId, userId);
                return true;
            }

            return false;
        }

        return true;
    }

    @Override
    public boolean undoLike(final int reviewId, final int userId) {
        log.debug("ReviewLikeDbStorage - undoLike()");

        String sqlRequest = "DELETE FROM review_like WHERE review_id = ? AND user_id = ? AND is_like = true;";
        int deleted = jdbcTemplate.update(sqlRequest, reviewId, userId);

        return deleted > 0;
    }

    @Override
    public boolean dislike(final int reviewId, final int userId) {
        log.debug("ReviewLikeDbStorage - dislike()");

        String sqlRequest = "INSERT INTO review_like (review_id, user_id, is_like) VALUES (?, ?, false);";

        try {
            jdbcTemplate.update(sqlRequest, reviewId, userId);
        } catch (DuplicateKeyException e) {
            sqlRequest = "SELECT is_like FROM review_like WHERE review_id = ? AND user_id = ?;";

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest, reviewId, userId);
            rowSet.next();

            boolean isLike = rowSet.getBoolean("is_like");

            if (isLike) {
                sqlRequest = "UPDATE review_like SET is_like = false WHERE review_id = ? AND user_id = ?";
                jdbcTemplate.update(sqlRequest, reviewId, userId);
                return true;
            }

            return false;
        }

        return true;
    }

    @Override
    public boolean undoDislike(final int reviewId, final int userId) {
        log.debug("ReviewLikeDbStorage - undoDislike()");

        String sqlRequest = "DELETE FROM review_like WHERE review_id = ? AND user_id = ? AND is_like = false;";
        int deleted = jdbcTemplate.update(sqlRequest, reviewId, userId);

        return deleted > 0;
    }
}
