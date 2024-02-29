package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genres.GenresStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenresStorage genresStorage;
    private final FilmStorage filmStorage;


    public List<Genre> getGenres() {
        log.debug("GenreService - service.getGenres()");
        return genresStorage.getAllGenres();
    }

    public List<Genre> getFilmGenres(int filmId) {
        log.debug("GenreService - service.getFilmGenres()");

        String message = "Фильма нет с id :" + filmId;
        filmIsExist(filmId, message);

        return genresStorage.getFilmGenre(filmId);
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
