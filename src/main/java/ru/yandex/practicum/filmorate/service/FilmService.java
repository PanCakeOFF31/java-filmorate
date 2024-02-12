package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.FilmDurationValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmNullValueValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmReleaseDateValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.restriction.FilmRestriction;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public List<Film> receiveFilms(int count) {
        log.debug("FilmService - service.getFilms()");

        if (count < 0) {
            log.info("Возвращен список фильмов в количестве: " + storage.getFilmsQuantity());
            return new ArrayList<>(storage.getFilms());
        }

        if (count > storage.getFilmsQuantity())
            count = storage.getFilmsQuantity();

        log.info("Возвращен список фильмов в количестве: " + count);

        List<Film> films = new ArrayList<>(storage.getFilms());
        Collections.shuffle(films);

        return films.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film addFilm(final Film film) {
        log.debug("FilmService - service.addFilm()");
        addValidation(film);

        Integer id = storage.addFilm(film);

        log.info("Добавлен фильм с Id: " + id);
        log.info(film.toString());
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
        log.debug("FilmService - service.getFilms()");
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
            throw new FilmNullValueValidationException(message);
        }

        if (!storage.containsFilm(film)) {
            String message = "Валидация на существование фильма по id не пройдена: " + film;
            log.warn(message);
            throw new FilmNullValueValidationException(message);
        }

        log.info("Успешное окончание updateValidation() валидации фильма: " + film);
        return true;
    }

}
