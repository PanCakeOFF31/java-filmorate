package ru.yandex.practicum.filmorate.storage.likes;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface LikeStorage {
    Set<Integer> getLikes(int filmId);
    Set<User> getUserLikes(int filmId);
}
