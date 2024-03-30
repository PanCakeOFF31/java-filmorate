package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.exception.MethodNotImplemented;
import ru.yandex.practicum.filmorate.exception.director.DirectorNotFounException;
import ru.yandex.practicum.filmorate.exception.review.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.filmReview.ReviewLikeDbStorage;
import ru.yandex.practicum.filmorate.storage.filmReview.ReviewStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    ReviewStorage reviewStorage;
    ReviewLikeDbStorage reviewLikeDbStorage;

    public Review createReview(final Review review) {
        log.debug("ReviewService - service.createReview()");
        return review;
//        throw new MethodNotImplemented("Метод добавления нового отзыва");
    }

    public Review updateReview(final Review review) {
        log.debug("ReviewService - service.updateReview()");
        throw new MethodNotImplemented("Метод редактирования уже имеющегося отзыва");
    }

    public Review deleteReviewById(final int reviewId) {
        log.debug("ReviewService - service.deleteReviewById({})", reviewId);
        throw new MethodNotImplemented("Метод удаления уже имеющегося отзыва");
//
//        reviewIsExist(reviewId);
//        log.info("Количество отзывов до удаления: {}", reviewStorage.getReviewQuantity());
//
//        Review deletedReview = reviewStorage.deleteReviewById(reviewId);
//        log.info("Количество отзывов после удаления: {}", reviewStorage.getReviewQuantity());
    }

    public Review getReviewById(final int reviewId) {
        log.debug("ReviewService - service.getReviewById({})", reviewId);

        throw new MethodNotImplemented("Метод получения отзыва по идентификатору");
    }

    public List<Review> getAllReviewsByFilmId(final int filmId,
                                              final int count) {
        log.debug("ReviewService - service.getAllReviewsByFilmId({}, {})", filmId, count);

        throw new MethodNotImplemented("Метод получения всех отзывов по идентификатору фильма");
    }

    public Review likeReview(final int reviewId,
                             final int userId) {
        log.debug("ReviewService - service.likeReview({}, {})", reviewId, userId);

        throw new MethodNotImplemented("Метод добавления лайка к отзыву");
    }

    public Review dislikeReview(final int reviewId,
                                final int userId) {
        log.debug("ReviewService - service.dislikeReview({}, {})", reviewId, userId);

        throw new MethodNotImplemented("Метод добавления дизлайка к отзыву");
    }

    public Review undoLikeReview(final int reviewId,
                                 final int userId) {
        log.debug("ReviewService - service.undoLikeReview({}, {})", reviewId, userId);

        throw new MethodNotImplemented("Метод удаления лайка к отзыву");
    }

    public Review undoDislikeReview(final int reviewId,
                                    final int userId) {
        log.debug("ReviewService - service.ReviewService({}, {})", reviewId, userId);

        throw new MethodNotImplemented("Метод удаления дизлайка к отзыву");
    }

    private void reviewIsExist(final int reviewId) {
        log.debug("ReviewService - service.reviewIsExist()");

        if (!reviewStorage.containsById(reviewId)) {
            String message = "Отзыва нет с id :" + reviewId;
            log.warn(message);
            throw new ReviewNotFoundException(message);
        }
    }
}
