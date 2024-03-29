package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.films.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genres.GenresStorage;
import ru.yandex.practicum.filmorate.storage.ratings.MpaStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenresStorage genresStorage;
    private final MpaStorage mpaStorage;
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

    public Genre getGenre(int genreId) {
        log.debug("GenreService - service.getGenre()");
        genreIsExist(genreId, "Такого жанра с id = " + genreId + " не существует в хранилище");
        return genresStorage.getGenre(genreId);
    }

    public boolean filmIsExist(int filmId, String message) {
        if (!filmStorage.containsById(filmId)) {
            log.warn(message);
            throw new FilmNotFoundException(message);
        }

        return true;
    }

    private boolean genreIsExist(int genreId, String message) {
        if (!genresStorage.containsById(genreId)) {
            log.warn(message);
            throw new GenreNotFoundException(message);
        }

        return true;
    }

}
