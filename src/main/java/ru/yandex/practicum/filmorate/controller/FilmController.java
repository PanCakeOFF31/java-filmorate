package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int generateId = 1;

    public int getFilmsSize() {
        return films.size();
    }

    @GetMapping
    public List<Film> receiveFilms() {
        log.debug("/films - GET: getFilms()");
        log.info("Возвращен список фильмов в количестве: " + films.size());

        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody final Film film) {
        log.debug("/films - POST: addFilm()");

        if (!addValidation(film)) {
            log.warn("Фильм не добавлен, так как не прошел валидацию");
            throw new ValidationException();
        }

        film.setId(generateId());
        films.put(generateId, film);

        log.info("Фильм добавлен: " + film);
        log.info("Количество фильмов: " + films.size());

        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody final Film film) {
        log.debug("/films - PUT: updateFilm()");

        if (!addValidation(film) || !updateValidation(film)) {
            log.warn("Фильм не обновлен, так как не прошел валидацию");
            throw new ValidationException();
        }

        films.put(film.getId(), film);

        log.info("Фильм обновлен: " + film);
        log.info("Количество фильмов: " + films.size());

        return film;
    }


    public boolean addValidation(final Film film) {
        log.debug("Начало addValidation() валидации фильма: " + film);

        final LocalDate releaseDate = film.getReleaseDate();
        final Duration duration = film.getDuration();

        boolean validReleaseDate = releaseDate.isAfter(LocalDate.of(1895, Month.DECEMBER, 28));

        if (!validReleaseDate) {
            log.warn("Валидация по дате выхода фильма не пройдена: " + film);
            return false;
        }

        boolean validDuration = duration.getSeconds() > 0;

        if (!validDuration) {
            log.warn("Валидация на положительную продолжительность фильма не пройдена: " + film);
            return false;
        }

        log.info("Успешное окончание addValidation() валидации фильма: " + film);

        return true;
    }

    public boolean updateValidation(final Film film) {
        log.debug("Начало updateValidation() валидации фильма: " + film);

        if (film.getId() == null) {
            log.warn("У фильма не id, валидация не пройдена: " + film);
            return false;
        }

        if (!films.containsKey(film.getId())) {
            log.warn("Валидация на существование фильма по id не пройдена: " + film);
            return false;
        }

        log.info("Успешное окончание updateValidation() валидации фильма: " + film);

        return true;
    }

    private int generateId() {
        while (films.containsKey(generateId))
            ++generateId;

        return this.generateId;
    }

}
