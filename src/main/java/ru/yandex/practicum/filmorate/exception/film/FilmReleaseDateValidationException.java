package ru.yandex.practicum.filmorate.exception.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;

public class FilmReleaseDateValidationException extends ValidationException {
    public FilmReleaseDateValidationException() {
        super();
    }

    public FilmReleaseDateValidationException(String message) {
        super(message);
    }
}
