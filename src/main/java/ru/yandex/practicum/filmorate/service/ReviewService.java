package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.review.ReviewUpdateValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ExistChecker;
import ru.yandex.practicum.filmorate.storage.filmReview.ReviewLikeDbStorage;
import ru.yandex.practicum.filmorate.storage.filmReview.ReviewStorage;

import java.util.List;
import java.util.function.BiFunction;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    final private ReviewStorage reviewStorage;
    final private ReviewLikeDbStorage reviewLikeDbStorage;
    final private ExistChecker existChecker;

    public Review createReview(final Review review) {
        log.debug("ReviewService - service.createReview()");

        createValidation(review);

        log.info("Количество отзывов до добавления: " + reviewStorage.getReviewQuantity());

        int id = reviewStorage.createReview(review);
        log.info("Добавлен отзыв с id: " + id);

        Review addedReview = reviewStorage.getReviewById(id);

        log.info(addedReview.toString());
        log.info("Количество режиссеров теперь: " + reviewStorage.getReviewQuantity());

        return addedReview;
    }

    public Review updateReview(final Review review) {
        log.debug("ReviewService - service.updateReview()");

        int reviewId = review.getId();
        existChecker.reviewIsExist(reviewId);

        createValidation(review);
        updateValidation(review);

        log.info("Отзыв до обновления: {}", reviewStorage.getReviewById(reviewId));

        Review updateReview = reviewStorage.updateReview(review);
        log.info("Отзыв после обновления: {}", updateReview);

        return updateReview;
    }

    public Review deleteReviewById(final int reviewId) {
        log.debug("ReviewService - service.deleteReviewById({})", reviewId);

        existChecker.reviewIsExist(reviewId);
        log.info("Количество отзывов до удаления: {}", reviewStorage.getReviewQuantity());

        Review deletedReview = reviewStorage.deleteReviewById(reviewId);
        log.info("Количество отзывов после удаления: {}", reviewStorage.getReviewQuantity());

        return deletedReview;
    }

    public Review getReviewById(final int reviewId) {
        log.debug("ReviewService - service.getReviewById({})", reviewId);

        existChecker.reviewIsExist(reviewId);

        return reviewStorage.getReviewById(reviewId);
    }

    public List<Review> getAllReviewsByFilmId(final int filmId,
                                              final int count) {
        log.debug("ReviewService - service.getAllReviewsByFilmId({}, {})", filmId, count);

        existChecker.filmIsExist(filmId);

        List<Review> reviews = reviewStorage.getAllReviewsByFilmId(filmId, count);
        log.info("Получены отзывы к фильму id: {} в количестве {} шт", filmId, reviews.size());

        return reviews;
    }

    public Review likeReview(final int reviewId, final int userId) {
        log.debug("ReviewService - service.likeReview({}, {})", reviewId, userId);

        return likeDislikeAlgorithm(reviewId, userId, reviewLikeDbStorage::like);
    }

    public Review dislikeReview(final int reviewId,
                                final int userId) {
        log.debug("ReviewService - service.dislikeReview({}, {})", reviewId, userId);

        return likeDislikeAlgorithm(reviewId, userId, reviewLikeDbStorage::dislike);
    }

    public Review undoLikeReview(final int reviewId,
                                 final int userId) {
        log.debug("ReviewService - service.undoLikeReview({}, {})", reviewId, userId);

        return likeDislikeAlgorithm(reviewId, userId, reviewLikeDbStorage::undoLike);
    }

    public Review undoDislikeReview(final int reviewId,
                                    final int userId) {
        log.debug("ReviewService - service.ReviewService({}, {})", reviewId, userId);

        return likeDislikeAlgorithm(reviewId, userId, reviewLikeDbStorage::undoDislike);
    }

    private Review likeDislikeAlgorithm(final int reviewId,
                                        final int userId,
                                        BiFunction<Integer, Integer, Boolean> operator) {
        log.debug("ReviewService - service.likeDislikeAlgorithm({}, {})", reviewId, userId);

        likeValidation(reviewId, userId);

        log.info("Полезность отзыва изначально: " + reviewLikeDbStorage.getUseful(reviewId));
        boolean result = operator.apply(reviewId, userId);

        String logMessage = result
                ? "Реакция пользователя id: " + userId + " установлена для отзыва id: " + reviewId
                : "Реакция пользователя id: " + userId + " уже установлен для отзыва id: " + reviewId;

        log.info(logMessage);
        log.info("Полезность отзыва теперь: " + reviewLikeDbStorage.getUseful(reviewId));

        return reviewStorage.getReviewById(reviewId);
    }

    private void createValidation(final Review review) {
        log.debug("ReviewService - service.createValidation()");
        log.debug("Валидации отзыва: " + review);

        int filmId = review.getFilmId();
        int userId = review.getUserId();

        existChecker.filmIsExist(filmId);
        existChecker.userIsExist(userId);

        log.info("Успешное окончание createValidation() валидации отзыва: " + review);
    }

    private void updateValidation(final Review review) {
        log.debug("ReviewService - service.updateValidation()");

        Review actualReview = reviewStorage.getReviewById(review.getId());

        int actualUserId = actualReview.getId();
        int actualFilmId = actualReview.getFilmId();

        boolean userIdEquals = actualUserId == review.getUserId();
        boolean filmIdEquals = actualFilmId == review.getFilmId();

        if (!(userIdEquals && filmIdEquals)) {
            log.warn("Актуальный отзыв: {}", actualReview);
            log.warn("Обновляемый отзыв: {}", review);

            String message = "Обновляемый отзыв не совпадает по идентификатору пользователя и фильма";
            log.warn(message);

            throw new ReviewUpdateValidationException(message);
        }

        log.info("Успешное окончание updateValidation() валидации отзыва: " + review);
    }

    private void likeValidation(final int reviewId, final int userId) {
        log.debug("ReviewService - service.likeValidation()");

        existChecker.reviewIsExist(reviewId);
        existChecker.userIsExist(userId);

        log.info("Успешное окончание likeValidation() для отзыва id: {} от пользователя id: {}", reviewId, userId);
    }
}
