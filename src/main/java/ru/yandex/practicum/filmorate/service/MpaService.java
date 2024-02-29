package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ratings.RatingDbStorage;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    private final RatingDbStorage ratingDbStorage;
    private final FilmStorage filmStorage;

    public Set<Mpa> getRatings() {
        log.debug("GenreService - service.getRatings()");
        return ratingDbStorage.getAllRating();
    }

    public Set<Mpa> getFilmRating(int filmId) {
        log.debug("GenreService - service.getFilmRating()");

        String message = "Фильма нет с id :" + filmId;
        filmIsExist(filmId, message);

        return ratingDbStorage.getFilmRating(filmId);
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
