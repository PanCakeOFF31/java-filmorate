package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.FilmDurationValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.film.FilmNullValueValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmReleaseDateValidationException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.restriction.FilmRestriction;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> receiveFilms(int count) {
        log.debug("FilmService - service.getFilms()");

        if (count < 0) {
            log.info("Возвращен список фильмов в количестве: " + filmStorage.getFilmsQuantity());
            return new ArrayList<>(filmStorage.getFilms());
        }

        if (count > filmStorage.getFilmsQuantity())
            count = filmStorage.getFilmsQuantity();

        List<Film> films = new ArrayList<>(filmStorage.getFilms());
        Collections.shuffle(films);

        log.info("Возвращен список фильмов в количестве: " + count);

        return films.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film receiveFilmById(int filmId) {
        if (!filmStorage.containsById(filmId)) {
            String message = "Фильма нет с id :" + filmId;
            log.warn(message);
            throw new FilmNotFoundException(message);
        }

        return filmStorage.getFilmById(filmId);
    }

    public Film addFilm(final Film film) {
        log.debug("FilmService - service.addFilm()");
        addValidation(film);

        Integer id = filmStorage.addFilm(film);
        film.setLikes(new HashSet<>());

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

        if (result)
            log.info("Лайк пользователя " + userId + " установлен для фильма " + filmId);
        else
            log.info("Лайк уже поставлен");

        log.info("Количеств лайка теперь: " + film.likeQuantity());

        return film;
    }

    public Film unlike(int filmId, int userId) {
        log.debug("FilmService - service.unlike()");
        likeValidation(filmId, userId);

        Film film = filmStorage.getFilmById(filmId);
        log.info("Количеств лайка в изначально:" + film.getLikes().size());
        boolean result = film.unlike(userId);

        if (result)
            log.info("Лайк пользователя " + userId + " снят с фильма " + filmId);
        else
            log.info("Лайк уже снят");

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

        if (!releaseDate.isAfter(FilmRestriction.REALEASE_DATE)) {
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

        if (film.getId() == null) {
            String message = "У фильма нет id, валидация не пройдена: " + film;
            log.warn(message);
            throw new FilmNullValueValidationException(message);
        }

        if (!filmStorage.containsFilm(film)) {
            String message = "Валидация на существование фильма по id не пройдена: " + film;
            log.warn(message);
            throw new FilmNotFoundException(message);
        }

        log.info("Успешное окончание updateValidation() валидации фильма: " + film);
        return true;
    }

    public boolean likeValidation(int filmId, int userId) {
        log.debug("FilmService - service.likeValidation()");

        if (!filmStorage.containsById(filmId)) {
            String message = "Фильма нет с id :" + filmId;
            log.warn(message);
            throw new FilmNotFoundException(message);
        }

        if (!userStorage.containsById(userId)) {
            String message = "Пользователя нет с id :" + userId;
            log.warn(message);
            throw new UserNotFoundException(message);
        }

        return true;
    }

}