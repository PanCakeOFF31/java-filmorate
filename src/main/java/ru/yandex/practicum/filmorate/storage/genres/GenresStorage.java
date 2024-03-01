package ru.yandex.practicum.filmorate.storage.genres;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenresStorage {
    boolean addFilmGenre(int filmId, int genreId);

    boolean deleteFilmGenre(int filmId, int genreId);
    boolean deleteAllFilmGenres(int filmId);

    List<Genre> getAllGenres();

    List<Genre> getFilmGenre(int filmId);
    Genre getGenre(int genreId);
    boolean containsById(int genreId);

}
