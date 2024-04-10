package ru.yandex.practicum.filmorate.storage.like;

import java.util.Map;
import java.util.Set;

public interface LikeStorage {
    boolean like(int filmId, int userId);

    boolean unlike(int filmId, int userId);

    int getLikes(int filmId);

    Map<Integer, Set<Integer>> getUsersFavoriteFilms();

}