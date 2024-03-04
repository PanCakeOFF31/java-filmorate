package ru.yandex.practicum.filmorate.storage.likes;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface LikeStorage {
    boolean like(int filmId, int userId);

    boolean unlike(int filmId, int userId);

    int getLikes(int filmId);

    Set<User> getLikesAsUsers(int filmId);
}
