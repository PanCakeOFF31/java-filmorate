package ru.yandex.practicum.filmorate.storage.filmReview;

public interface ReviewLikeStorage {
    int getUseful(final int reviewId);

    boolean like(final int reviewId, final int userId);

    boolean undoLike(final int reviewId, final int userId);

    boolean dislike(final int reviewId, final int userId);

    boolean undoDislike(final int reviewId, final int userId);

}
