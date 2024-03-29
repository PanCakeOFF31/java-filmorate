package ru.yandex.practicum.filmorate.storage.likes;

public interface LikeStorage {
    boolean like(int filmId, int userId);

    boolean unlike(int filmId, int userId);

    int getLikes(int filmId);

}