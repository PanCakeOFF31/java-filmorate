package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    int getFilmsQuantity();

    List<Film> getFilms();

    List<Film> getFilms(int count);

    Film getFilmById(int id);

    Integer addFilm(Film film);

    boolean containsById(int id);

    Film updateFilm(Film film);

    List<Film> getTopFilms(int size);

    List<Film> getSortedDirectorFilmsBy(final int directorId, final String sortBy);

    List<Film> getTopFilmsBySubstringOnTitle(String condition);

    List<Film> getTopFilmsByCondition(String condition);

    List<Film> getTopFilmsByDirector(List<Integer> directors);

    Film deleteFilmById(int id);
}
