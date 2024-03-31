package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.mpa.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.review.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmDirector.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.filmGenre.GenresStorage;
import ru.yandex.practicum.filmorate.storage.filmMpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.filmRate.RateStorage;
import ru.yandex.practicum.filmorate.storage.filmReview.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.userEvent.EventStorage;
import ru.yandex.practicum.filmorate.storage.userEvent.event_type.EventTypeStorage;
import ru.yandex.practicum.filmorate.storage.userEvent.operation.OperationStorage;

@RequiredArgsConstructor
@Slf4j
@Component
public class ExistChecker {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenresStorage genresStorage;
    private final MpaStorage mpaStorage;
    private final ReviewStorage reviewStorage;
    private final DirectorStorage directorStorage;
    private final RateStorage rateStorage;
    private final EventStorage eventStorage;
    private final EventTypeStorage eventTypeStorage;
    private final OperationStorage operationStorage;

    public void filmIsExist(int filmId) {
        log.debug("ExistChecker - service.filmIsExist()");

        if (!filmStorage.containsById(filmId)) {
            String message = "Такого фильма с id = " + filmId + " не существует в хранилище";

            log.warn(message);
            throw new FilmNotFoundException(message);
        }
    }

    public void userIsExist(int userId) {
        log.debug("ExistChecker - service.userIsExist()");

        if (!userStorage.containsById(userId)) {
            String message = "Такого пользователя с id = " + userId + " не существует в хранилище";
            log.warn(message);
            throw new UserNotFoundException(message);
        }
    }

    public void genreIsExist(int genreId) {
        log.debug("ExistChecker - service.genreIsExist()");

        if (!genresStorage.containsById(genreId)) {
            String message = "Такого жанра с id = " + genreId + " не существует в хранилище";
            log.warn(message);
            throw new GenreNotFoundException(message);
        }
    }

    public void mpaIsExist(int mpaId) {
        log.debug("ExistChecker - service.mpaIsExist()");

        if (!mpaStorage.containsById(mpaId)) {
            String message = "Такого рейтинга с id = " + mpaId + " не существует в хранилище";
            log.warn(message);
            throw new MpaNotFoundException(message);
        }
    }

    public void reviewIsExist(int reviewId) {
        log.debug("ExistChecker - service.reviewIsExist()");

        if (!reviewStorage.containsById(reviewId)) {
            String message = "Такого отзыва с id = " + reviewId + " не существует в хранилище";
            log.warn(message);
            throw new ReviewNotFoundException(message);
        }
    }
}
