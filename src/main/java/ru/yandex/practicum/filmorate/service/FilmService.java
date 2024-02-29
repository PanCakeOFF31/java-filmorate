package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.FilmDurationValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.film.FilmNullValueValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmReleaseDateValidationException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.restriction.FilmRestriction;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genres.GenresStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikeStorage;
import ru.yandex.practicum.filmorate.storage.ratings.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final GenresStorage genresStorage;
    public List<Film> receiveFilms(int count) {
        log.debug("FilmService - service.getFilms()");
        int filmsQuantity = filmStorage.getFilmsQuantity();

        if (count < 0) {
            log.info("Возвращен список фильмов в количестве: " + filmsQuantity);
            return new ArrayList<>(filmStorage.getFilms());
        }

        if (count > filmsQuantity)
            count = filmsQuantity;

        List<Film> films = new ArrayList<>(filmStorage.getFilms());
        Collections.shuffle(films);

        log.info("Возвращен список фильмов в количестве: " + count);

        return films.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film receiveFilmById(int filmId) {
        log.debug("FilmService - service.receiveFilmById()");

        String message = "Фильма нет с id :" + filmId;
        filmIsExist(filmId, message);

        return filmStorage.getFilmById(filmId);
    }

    public Film addFilm(final Film film) {
        log.debug("FilmService - service.addFilm()");
        addValidation(film);

        film.setLikes(new HashSet<>());
        film.setGenres(new HashSet<>());
        Integer id = filmStorage.addFilm(film);

        log.info("Добавлен фильм с Id: " + id);
        log.info(film.toString());
        log.info("Количество фильмов теперь: " + filmStorage.getFilmsQuantity());

        return film;
    }

    public Film updateFilm(final Film film) {
        log.debug("FilmService - service.updateFilm()");
        addValidation(film);
        updateValidation(film);

//        Так как не допускается передавать список лайков, при обновлении нужно сохранять его
        Set<Integer> likes = filmStorage.getFilmById(film.getId()).getLikes();
        film.setLikes(likes);

        filmStorage.updateFilm(film);

        log.info("Фильм обновлен: " + film);
        log.info("Количество фильмов: " + filmStorage.getFilmsQuantity());

        return film;
    }

    public Film like(int filmId, int userId) {
        log.debug("FilmService - service.like()");
        likeValidation(filmId, userId);

        Film film = filmStorage.getFilmById(filmId);
        log.info("Количеств лайка в изначально:" + film.likeQuantity());
        boolean result = film.like(userId);

        String logMessage = result
                ? "Лайк пользователя " + userId + " установлен для фильма " + filmId
                : "Лайк уже поставлен";

        log.info(logMessage);
        log.info("Количеств лайка теперь: " + film.likeQuantity());

        return film;
    }

    public Film unlike(int filmId, int userId) {
        log.debug("FilmService - service.unlike()");
        likeValidation(filmId, userId);

        Film film = filmStorage.getFilmById(filmId);
        log.info("Количеств лайка в изначально:" + film.getLikes().size());
        boolean result = film.unlike(userId);

        String logMessage = result
                ? "Лайк пользователя " + userId + " снят с фильма " + filmId
                : "Лайк уже снят";

        log.info(logMessage);
        log.info("Количеств лайка теперь: " + film.getLikes().size());

        return film;
    }

    public List<Film> getTop(int size) {
        log.debug("FilmService - service.getTop()");

        List<Film> films = filmStorage.getFilms().stream()
                .sorted(Comparator.comparing(Film::likeQuantity).reversed())
                .limit(size)
                .collect(Collectors.toList());

        if (size > films.size())
            size = films.size();

        log.info("Возвращен топ фильмов по лайкам в количестве: " + size);
        return films;
    }

    public boolean addValidation(final Film film) {
        log.debug("FilmService - service.addValidation()");
        log.debug("Валидации фильма: " + film);

        final LocalDate releaseDate = film.getReleaseDate();
        final Duration duration = film.getDuration();

        if (!releaseDate.isAfter(FilmRestriction.RELEASE_DATE)) {
            String message = "Валидация по дате выхода фильма не пройдена: " + film;
            log.warn(message);
            throw new FilmReleaseDateValidationException(message);
        }

        if (!(duration.getSeconds() > FilmRestriction.MIN_DURATION)) {
            String message = "Валидация на положительную продолжительность фильма не пройдена: " + film;
            log.warn(message);
            throw new FilmDurationValidationException(message);
        }

        log.info("Успешное окончание addValidation() валидации фильма: " + film);
        return true;
    }

    public boolean updateValidation(final Film film) {
        log.debug("FilmService - service.updateValidation()");
        log.debug("Валидации фильма: " + film);
        String message;

        if (film.getId() == null) {
            message = "У фильма нет id, валидация не пройдена: " + film;
            log.warn(message);
            throw new FilmNullValueValidationException(message);
        }

        message = "Валидация на существование фильма по id не пройдена: " + film;
        filmIsExist(film.getId(), message);

        log.info("Успешное окончание updateValidation() валидации фильма: " + film);
        return true;
    }

    public boolean likeValidation(int filmId, int userId) {
        log.debug("FilmService - service.likeValidation()");
        String message;

        message = "Фильма нет с id :" + filmId;
        filmIsExist(filmId, message);

        message = "Пользователя нет с id :" + userId;
        userIsExist(userId, message);

        return true;
    }

    public boolean filmIsExist(int filmId, String message) {
        if (!filmStorage.containsById(filmId)) {
            message = "Фильма нет с id :" + filmId;
            log.warn(message);
            throw new FilmNotFoundException(message);
        }

        return true;
    }

    private boolean userIsExist(int userId, String message) {
        if (!userStorage.containsById(userId)) {
            log.warn(message);
            throw new UserNotFoundException(message);
        }

        return true;
    }
}