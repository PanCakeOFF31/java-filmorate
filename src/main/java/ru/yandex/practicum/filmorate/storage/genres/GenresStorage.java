package ru.yandex.practicum.filmorate.storage.genres;

import ru.yandex.practicum.filmorate.model.Genre.Genre;
import ru.yandex.practicum.filmorate.model.Genre.GenreId;

import java.util.List;

public interface GenresStorage {
    void addFilmGenre(int filmId, int genreId);

    void deleteFilmGenre(int filmId, int genreId);

    List<Genre> getAllGenres();

    List<Genre> getFilmGenre(int filmId);

    List<GenreId> getFilmGenreId(int filmId);

}
