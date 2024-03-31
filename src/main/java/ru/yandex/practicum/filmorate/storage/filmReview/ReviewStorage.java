package ru.yandex.practicum.filmorate.storage.filmReview;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    int createReview(final Review review);

    Review updateReview(final Review review);

    Review deleteReviewById(final int reviewId);

    boolean containsById(final int reviewId);

    List<Review> getAllReviewsByFilmId(final int filmId, final int count);

    int getReviewQuantity();

    Review getReviewById(final int reviewId);

}
