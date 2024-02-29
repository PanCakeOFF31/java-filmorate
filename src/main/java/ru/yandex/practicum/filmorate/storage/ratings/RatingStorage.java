package ru.yandex.practicum.filmorate.storage.ratings;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Set;

public interface RatingStorage {
    Set<Mpa> getAllRating();

    Set<Mpa> getFilmRating(int filmId);
}
