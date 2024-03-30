package ru.yandex.practicum.filmorate.storage.filmReview;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewLikeDbStorage implements ReviewLikeStorage {
    @Override
    public int getLikes(final int reviewId) {
        log.debug("ReviewLikeDbStorage - getLikes()");

        return 0;
    }

    @Override
    public int getDislikes(final int reviewId) {
        log.debug("ReviewLikeDbStorage - getDislikes()");

        return 0;
    }

    @Override
    public int getUseful(final int reviewId) {
        log.debug("ReviewLikeDbStorage - getUseful()");

        return 0;
    }

    @Override
    public boolean like(final int reviewId, final int userId) {
        log.debug("ReviewLikeDbStorage - like()");

//        TODO: добавить true в запросе SQL
        return false;
    }

    @Override
    public boolean undoLike(final int reviewId, final int userId) {
        log.debug("ReviewLikeDbStorage - undoLike()");

        return false;
    }

    @Override
    public boolean dislike(final int reviewId, final int userId) {
        log.debug("ReviewLikeDbStorage - dislike()");

//        TODO: добавить false в запросе SQL
        return false;
    }

    @Override
    public boolean undoDislike(final int reviewId, final int userId) {
        log.debug("ReviewLikeDbStorage - undoDislike()");

        return false;
    }

    @Override
    public boolean containsByReviewId(int reviewId) {
        log.debug("ReviewLikeDbStorage - containsByReviewId()");

        return false;
    }

    @Override
    public boolean containsByReviewUserId(int reviewId, int userId) {
        log.debug("ReviewLikeDbStorage - containsByReviewUserId()");

        return false;
    }
}
