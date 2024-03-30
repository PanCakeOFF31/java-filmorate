package ru.yandex.practicum.filmorate.storage.filmDirector;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface DirectorStorage {
    List<Director> getFilmsDirector(final int filmId);

    Integer createDirector(final Director director);

    Director getDirectorById(final int directorId);

    Director changeDirector(final Director director);

    Director deleteDirectorById(final int directorId);

    List<Director> getDirectors();

    List<Film> getSortedDirectorFilmsBy(final int directorId, final String sortBy);

    int getDirectorQuantity();

    boolean containsById(final int directorId);

}
