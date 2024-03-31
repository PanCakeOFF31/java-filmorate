package ru.yandex.practicum.filmorate.storage.filmReview;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    private final ReviewLikeStorage reviewLikeStorage;

    @Override
    public int createReview(Review review) {
        log.debug("ReviewDbStorage - createReview()");

        int userId = review.getUserId();
        int filmId = review.getFilmId();
        String content = review.getContent();
        boolean isPositive = review.isPositive();

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film_review")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();

        params.put("user_id", userId);
        params.put("film_id", filmId);
        params.put("content", content);
        params.put("is_positive", isPositive);

        int reviewId = jdbcInsert.executeAndReturnKey(params).intValue();

        log.info("Пользователь id: {} на фильм id: {} добавил:", userId, filmId);

        if (isPositive)
            log.info("Положительный отзыв с id: {}", reviewId);
        else
            log.info("Отрицательный отзыв с id: {}", reviewId);

        return reviewId;
    }

    @Override
    public Review updateReview(Review review) {
        log.debug("ReviewDbStorage - updateReview()");

        int reviewId = review.getId();

        String sqlRequest = "UPDATE film_review SET content = ?, is_positive = ? WHERE id = ?";
        jdbcTemplate.update(sqlRequest, review.getContent(), review.isPositive(), reviewId);

        return getReviewById(reviewId);
    }

    @Override
    public Review deleteReviewById(int reviewId) {
        log.debug("ReviewDbStorage - deleteReviewById()");

        Review deletedReview = getReviewById(reviewId);

        String sqlRequest = "DELETE FROM film_review WHERE id = ?";
        jdbcTemplate.update(sqlRequest, reviewId);

        return deletedReview;
    }

    @Override
    public Review getReviewById(int reviewId) {
        log.debug("ReviewDbStorage - getReviewById()");

        String sqlRequest = "SELECT * FROM film_review WHERE id = ?";
        RowMapper<Review> reviewMapper = (rs, rowNum) -> makeReview(rs);

        return jdbcTemplate.queryForObject(sqlRequest, reviewMapper, reviewId);
    }

    @Override
    public int getReviewQuantity() {
        log.debug("ReviewDbStorage - getReviewQuantity()");

        String sqlRequest = "SELECT COUNT(*) AS review_quantity FROM film_review";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest);

        if (rowSet.next())
            return rowSet.getInt("review_quantity");

        return 0;
    }

    @Override
    public List<Review> getAllReviewsByFilmId(int filmId, int count) {
        log.debug("ReviewDbStorage - getAllReviewsByFilmId()");

        String sqlRequest = "SELECT * FROM film_review WHERE film_id = ? LIMIT ?";
        RowMapper<Review> reviewMapper = (rs, rowNum) -> makeReview(rs);

        return jdbcTemplate.query(sqlRequest, reviewMapper, filmId, count);
    }

    @Override
    public boolean containsById(int reviewId) {
        log.debug("ReviewDbStorage - containsById()");

        String sqlRequest = "SELECT * FROM film_review WHERE id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest, reviewId);

        return rowSet.next();
    }

    public Review makeReview(ResultSet rs) throws SQLException {
        log.debug("ReviewDbStorage - makeReview()");

        final int id = rs.getInt("id");
        final int userId = rs.getInt("user_id");
        final int film_id = rs.getInt("film_id");
        final String content = rs.getString("content");
        final boolean isPositive = rs.getBoolean("is_positive");
        final int useful = reviewLikeStorage.getUseful(id);

        return new Review(id, content, isPositive, userId, film_id, useful);
    }
}
