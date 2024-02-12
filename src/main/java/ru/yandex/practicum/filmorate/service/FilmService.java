package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.FilmDurationValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmNullValueException;
import ru.yandex.practicum.filmorate.exception.film.FilmReleaseDateValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.restriction.FilmRestriction;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public List<Film> receiveFilms() {
        log.debug("FilmService - service.getFilms()");
        log.info("Возвращен список фильмов в количестве: " + storage.getFilmsQuantity());

        return new ArrayList<>(storage.getFilms());
    }

    public Film addFilm(final Film film) {
        log.debug("FilmService - service.addFilm()");
        addValidation(film);

        Integer id = storage.addFilm(film);

        log.info("Добавлен фильм с Id: " + id);
        log.info("Количество фильмов: " + storage.getFilmsQuantity());

        return film;
    }

    public Film updateFilm(final Film film) {
        log.debug("FilmService - service.updateFilm()");
        addValidation(film);
        updateValidation(film);

        storage.updateFilm(film);

        log.info("Фильм обновлен: " + film);
        log.info("Количество фильмов: " + storage.getFilmsQuantity());

        return film;
    }

    public Film like(int filmId, int userId) {
        return null;
    }

    public Film unlike(int filmId, int userId) {
        return null;
    }

    public List<Film> getTop(int size) {
        return null;
    }

    public boolean addValidation(final Film film) {
        log.debug("Начало addValidation() валидации фильма: " + film);

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
        log.debug("Начало updateValidation() валидации фильма: " + film);

        if (film.getId() == null) {
            String message = "У фильма нет id, валидация не пройдена: " + film;
            log.warn(message);
            throw new FilmNullValueException(message);
        }

        if (!storage.containsFilm(film)) {
            String message = "Валидация на существование фильма по id не пройдена: " + film;
            log.warn(message);
            throw new FilmNullValueException(message);
        }

        log.info("Успешное окончание updateValidation() валидации фильма: " + film);
        return true;
    }

}
