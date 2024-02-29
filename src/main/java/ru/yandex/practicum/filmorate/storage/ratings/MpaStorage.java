package ru.yandex.practicum.filmorate.storage.ratings;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> getAllMpa();
    Mpa getFilmMpa(int filmId);
    Mpa getMpa(int mpaId);
}
