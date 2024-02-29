package ru.yandex.practicum.filmorate.storage.genres;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenreId;

import java.util.List;
import java.util.Set;

public interface GenresStorage {
    void addFilmGenre(int filmId, int genreId);

    void deleteFilmGenre(int filmId, int genreId);

    List<Genre> getAllGenres();

    List<Genre> getFilmGenre(int filmId);

    List<GenreId> getFilmGenreId(int filmId);

}
