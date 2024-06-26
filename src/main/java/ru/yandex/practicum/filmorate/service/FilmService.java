package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InvalidRequestParameterValue;
import ru.yandex.practicum.filmorate.exception.director.DirectorNotFounException;
import ru.yandex.practicum.filmorate.exception.film.FilmDurationValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmNullValueValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmReleaseDateValidationException;
import ru.yandex.practicum.filmorate.exception.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.mpa.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.restriction.FilmRestriction;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenresStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final GenresStorage genresStorage;
    private final MpaStorage mpaStorage;
    private final ExistChecker existChecker;
    private final DirectorStorage directorStorage;

    public List<Film> receiveFilms(int count) {
        log.debug("FilmService - service.getFilms()");
        int filmsQuantity = filmStorage.getFilmsQuantity();

        if (count < 0) {
            log.info("Возвращен список фильмов в количестве: " + filmsQuantity);
            return new ArrayList<>(filmStorage.getFilms());
        }

        if (count > filmsQuantity)
            count = filmsQuantity;

        List<Film> films = new ArrayList<>(filmStorage.getFilms(count));

        log.info("Возвращен список фильмов в количестве: " + count);

        return films;
    }

    public Film receiveFilmById(int filmId) {
        log.debug("FilmService - service.receiveFilmById({})", filmId);

        existChecker.filmIsExist(filmId);

        return filmStorage.getFilmById(filmId);
    }

    public Film addFilm(final Film film) {
        log.debug("FilmService - service.addFilm()");

        correctGenres(film);
        correctDirectors(film);
        addValidation(film);

        int id = filmStorage.addFilm(film);

        log.info("Добавлен фильм с Id: " + id);
        Film addedFilm = filmStorage.getFilmById(id);
        log.info(film.toString());
        log.info("Количество фильмов теперь: " + filmStorage.getFilmsQuantity());

        return addedFilm;
    }

    public Film updateFilm(final Film film) {
        log.debug("FilmService - service.updateFilm()");

        correctGenres(film);
        correctDirectors(film);
        updateValidation(film);
        addValidation(film);

        Film updatedFilm = filmStorage.updateFilm(film);

        log.info("Фильм обновлен: " + updatedFilm);
        log.info("Количество фильмов: " + filmStorage.getFilmsQuantity());

        return updatedFilm;
    }

    public Film deleteFilmById(final int filmId) {
        log.debug("FilmService - service.deleteFilm()");

        Film deletedFilm = filmStorage.deleteFilmById(filmId);

        log.info("Фильм удален: " + deletedFilm);
        log.info("Количество фильмов: " + filmStorage.getFilmsQuantity());

        return deletedFilm;
    }

    public Film like(int filmId, int userId) {
        log.debug("FilmService - service.like()");
        likeValidation(filmId, userId);

        log.info("Количеств лайка в изначально:" + likeStorage.getLikes(filmId));
        boolean result = likeStorage.like(filmId, userId);

        String logMessage = result
                ? "Лайк пользователя " + userId + " установлен для фильма " + filmId
                : "Лайк уже поставлен";

        log.info(logMessage);
        log.info("Количеств лайка теперь: " + likeStorage.getLikes(filmId));

        return filmStorage.getFilmById(filmId);
    }

    public Film unlike(final int filmId, final int userId) {
        log.debug("FilmService - service.unlike()");
        likeValidation(filmId, userId);

        log.info("Количеств лайка в изначально:" + likeStorage.getLikes(filmId));
        boolean result = likeStorage.unlike(filmId, userId);

        String logMessage = result
                ? "Лайк пользователя " + userId + " снят с фильма " + filmId
                : "Лайк уже снят";

        log.info(logMessage);
        log.info("Количеств лайка теперь: " + likeStorage.getLikes(filmId));

        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getTop(int size) {
        log.debug("FilmService - service.getTop()");

        int filmsQuantity = filmStorage.getFilmsQuantity();

        if (size > filmsQuantity)
            size = filmsQuantity;

        log.info("Возвращен топ фильмов по лайкам в количестве: " + size);

        return filmStorage.getTopFilms(size);
    }

    public List<Film> getCommonFilms(int userId, int friendId) {
        log.debug("FilmService - service.getCommonFilms()");
        return filmStorage.getCommonFilms(userId, friendId);
    }

    public List<Film> searchFilmByCondition(String query, List<String> by) {
        log.debug("FilmService - searchFilmByCondition()");

        if (by.size() == 1) {
            if (by.contains("director")) {
                List<Integer> directorsId = directorStorage.getDirectorsIdBySubstringOnName(query);
                List<Film> listTopFilmByDirectors = new ArrayList<>();

                if (!directorsId.isEmpty()) {
                    listTopFilmByDirectors = filmStorage.getTopFilmsByDirector(directorsId);
                }
                return listTopFilmByDirectors;
            }
            if (by.contains("title")) {
                return filmStorage.getTopFilmsBySubstringOnTitle(query);
            }
        }

        if (by.contains("director") && by.contains("title") && by.size() == 2) {
            return filmStorage.getTopFilmsByCondition(query);
        }

        return List.of();
    }

    public List<Film> getTopFilmsByYearAndGenre(int count, int genreId, int year) {
        log.debug("FilmService - getTopFilmsByYearAndGenre()");
        existChecker.genreIsExist(genreId);
        return filmStorage.getTopFilmsByYearAndGenre(count, genreId, year);
    }

    public List<Film> getTopFilmsByGenre(int count, int genreId) {
        log.debug("FilmService - getTopFilmsByGenre()");
        return filmStorage.getTopFilmsByGenre(count, genreId);
    }

    public List<Film> getTopFilmsByYear(int count, int year) {
        log.debug("FilmService - getTopFilmsByYear()");
        return filmStorage.getTopFilmsByYear(count, year);
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

        int mpaId = film.getMpa().getId();
        if (!mpaStorage.containsById(mpaId)) {
            String message = "Такого рейтинга с id = " + mpaId + " не существует в хранилище";
            log.warn(message);
            throw new MpaNotFoundException(message);
        }

        film.getGenres().forEach(genre -> {
            int genreId = genre.getId();
            if (!genresStorage.containsById(genreId)) {
                String message = "Такого жанра с id = " + genreId + " не существует в хранилище";
                log.warn(message);
                throw new GenreNotFoundException(message);
            }
        });

        film.getDirectors().forEach(director -> {
            int directorId = director.getId();
            if (!directorStorage.containsById(directorId)) {
                String message = "Такого режиссера с id = " + directorId + " не существует в хранилище";
                log.warn(message);
                throw new DirectorNotFounException(message);
            }
        });

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
        existChecker.filmIsExist(film.getId(), message);

        log.info("Успешное окончание updateValidation() валидации фильма: " + film);
        return true;
    }

    public List<Film> receiveSortedDirectorFilmsBy(final int directorId, final String sortBy) {
        log.debug("DirectorService - service.receiveSortedDirectorFilmsBy()");

        existChecker.directorIsExist(directorId);
        checkRequestParam(sortBy);

        List<Film> directorFilms = filmStorage.getSortedDirectorFilmsBy(directorId, sortBy);

        log.info("Возвращен сортированный список по {} режиссера в количестве: {} шт",
                sortBy,
                directorFilms.size());

        return directorFilms;
    }

    public boolean likeValidation(int filmId, int userId) {
        log.debug("FilmService - service.likeValidation()");

        existChecker.filmIsExist(filmId);
        existChecker.userIsExist(userId);
        return true;
    }

    private void checkRequestParam(final String sortBy) {
        log.debug("FilmService - service.checkRequestParam()");

        String message = "Недопустимое значение параметра запроса " + sortBy;

        switch (sortBy) {
            case "year":
            case "likes":
                break;
            default:
                throw new InvalidRequestParameterValue(message);
        }
    }

    private void correctGenres(final Film film) {
        log.debug("FilmService - service.correctGenres()");

        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
            log.info("Жанры инициализированы пустым списком");
            return;
        }

        log.info("У фильма указаны жанры, коррекции не было");
        film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
    }

    private void correctDirectors(final Film film) {
        log.debug("FilmService - service.correctDirectors()");

        if (film.getDirectors() == null) {
            film.setDirectors(new ArrayList<>());
            log.info("Режиссеры инициализированы пустым списком");
            return;
        }

        log.info("У фильма указаны режиссеры, коррекции не было");
        film.setDirectors(film.getDirectors().stream().distinct().collect(Collectors.toList()));
    }
}