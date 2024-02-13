package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    int getFilmsQuantity();
    List<Film> getFilms();
    Set<Integer> getKeys();
    Film getFilmById(int id);
    Integer addFilm(Film film);
    boolean containsFilm(Film film);
    boolean containsById(int id);
    Film updateFilm(Film film);
}
