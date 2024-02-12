package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    int getFilmsQuantity();
    List<Film> getFilms();
    Integer addFilm(Film film);
    boolean containsFilm(Film film);
    void updateFilm(Film film);
}
