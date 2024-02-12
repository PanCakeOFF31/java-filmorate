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
        log.debug("/films - GET: getFilms()");
        log.info("Возвращен список фильмов в количестве: " + storage.getFilmsSize());

        return new ArrayList<>(storage.values());
    }

    public Film addFilm(final Film film) {
        if (addValidation(film))
            log.info("Успешное окончание addValidation() валидации фильма: " + film);

        film.setId(generateId());
        storage.put(generateId, film);

        log.info("Фильм добавлен: " + film);
        log.info("Количество фильмов: " + films.size());

        return film;
    }

    public Film updateFilm(final Film film) {
        if (addValidation(film) && updateValidation(film))
            log.info("Успешное окончание updateValidation() валидации фильма: " + film);

        films.put(film.getId(), film);

        log.info("Фильм обновлен: " + film);
        log.info("Количество фильмов: " + films.size());

        return film;
    }

    public boolean addValidation(final Film film) {
        log.debug("Начало addValidation() валидации фильма: " + film);

        final LocalDate releaseDate = film.getReleaseDate();
        final Duration duration = film.getDuration();

        boolean validReleaseDate = releaseDate.isAfter(FilmRestriction.REALEASE_DATE);

        if (!validReleaseDate) {
            String message = "Валидация по дате выхода фильма не пройдена: " + film;
            log.warn(message);
            throw new FilmReleaseDateValidationException(message);
        }

        boolean validDuration = duration.getSeconds() > FilmRestriction.MIN_DURATION;

        if (!validDuration) {
            log.warn("Валидация на положительную продолжительность фильма не пройдена: " + film);
            throw new FilmDurationValidationException();
        }

        return true;
    }

    public boolean updateValidation(final Film film) {
        log.debug("Начало updateValidation() валидации фильма: " + film);

        if (film.getId() == null) {
            log.warn("У фильма не id, валидация не пройдена: " + film);
            throw new FilmNullValueException("");
        }

        if (!films.containsKey(film.getId())) {
            log.warn("Валидация на существование фильма по id не пройдена: " + film);
            throw new FilmNullValueException();
        }

        return true;
    }
}
