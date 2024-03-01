package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.films.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ratings.MpaDbStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage ratingDbStorage;
    private final FilmStorage filmStorage;

    public List<Mpa> getMpas() {
        log.debug("GenreService - service.getMpas()");
        return ratingDbStorage.getAllMpa();
    }

    public Mpa getFilmMpa(int filmId) {
        log.debug("GenreService - service.getFilmMpa()");

        String message = "Фильма нет с id :" + filmId;
        filmIsExist(filmId, message);

        return ratingDbStorage.getFilmMpa(filmId);
    }

    public Mpa getMpaById(int mpaId) {
        log.debug("GenreService - service.getMpaById()");
        mpaIsExist(mpaId, "Такого рейтинга с id = " + mpaId + " не существует в хранилище");
        return ratingDbStorage.getMpa(mpaId);
    }

    public boolean filmIsExist(int filmId, String message) {
        if (!filmStorage.containsById(filmId)) {
            log.warn(message);
            throw new FilmNotFoundException(message);
        }

        return true;
    }

    private boolean mpaIsExist(int mpaId, String message) {
        if (!ratingDbStorage.containsById(mpaId)) {
            log.warn(message);
            throw new MpaNotFoundException(message);
        }

        return true;
    }
}
