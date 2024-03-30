package ru.yandex.practicum.filmorate.storage.filmReview;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public int createReview(Review review) {
        log.debug("ReviewDbStorage - createReview()");

        return 0;
    }

    @Override
    public Review updateReview(Review review) {
        log.debug("ReviewDbStorage - updateReview()");

        return null;
    }

    @Override
    public Review deleteReviewById(int reviewId) {
        log.debug("ReviewDbStorage - deleteReviewById()");

        return null;
    }

    @Override
    public int getReviewQuantity() {
        log.debug("ReviewDbStorage - getReviewQuantity()");

        return 0;
    }

    @Override
    public List<Review> getAllReviewsByFilmId(int filmId, int count) {
        log.debug("ReviewDbStorage - getAllReviewsByFilmId()");
//  SELECT * FROM film_review WHERE film_id = 10
        return null;
    }

    @Override
    public boolean containsById(int reviewId) {
        log.debug("ReviewDbStorage - containsById()");

        return false;
    }
}
