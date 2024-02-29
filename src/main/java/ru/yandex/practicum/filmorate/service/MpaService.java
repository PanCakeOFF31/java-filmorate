package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
        log.debug("GenreService - service.getRatings()");
        return ratingDbStorage.getAllMpa();
    }

    public Mpa getFilmMpa(int filmId) {
        log.debug("GenreService - service.getFilmRating()");

        String message = "Фильма нет с id :" + filmId;
        filmIsExist(filmId, message);

        return ratingDbStorage.getFilmMpa(filmId);
    }

    public Mpa getMpa(int mpaId) {
        log.debug("GenreService - service.getFilmRating()");
        return ratingDbStorage.getMpa(mpaId);
    }

    public boolean filmIsExist(int filmId, String message) {
        if (!filmStorage.containsById(filmId)) {
            message = "Фильма нет с id :" + filmId;
            log.warn(message);
            throw new FilmNotFoundException(message);
        }

        return true;
    }
}
