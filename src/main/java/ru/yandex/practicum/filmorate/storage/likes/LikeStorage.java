package ru.yandex.practicum.filmorate.storage.likes;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface LikeStorage {
    void like(int userId, int filmId);
    void unlike(int userId, int filmId);
    Set<Integer> getLikes(int filmId);
    Set<User> getLikesAsUsers(int filmId);
}
