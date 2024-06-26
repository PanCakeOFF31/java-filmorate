package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    List<Director> getFilmsDirector(final int filmId);

    Integer createDirector(final Director director);

    Director getDirectorById(final int directorId);

    Director changeDirector(final Director director);

    Director deleteDirectorById(final int directorId);

    List<Director> getDirectors();

    List<Integer> getDirectorsIdBySubstringOnName(String condition);

    int getDirectorQuantity();

    boolean containsById(final int directorId);

    boolean addFilmDirector(int filmId, int directorId);

    boolean deleteAllFilmDirectors(int filmId);
}
