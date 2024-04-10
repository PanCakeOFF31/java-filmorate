package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> getAllMpa();

    Mpa getFilmMpa(int filmId);

    Mpa getMpa(int mpaId);

    boolean containsById(int mpaId);
}
