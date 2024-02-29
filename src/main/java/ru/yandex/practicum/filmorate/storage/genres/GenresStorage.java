package ru.yandex.practicum.filmorate.storage.genres;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenresStorage {
    void addFilmGenre(int filmId, int genreId);

    void deleteFilmGenre(int filmId, int genreId);

    Set<Genre> getAllGenres();

    Set<Genre> getFilmGenre(int filmId);

}
