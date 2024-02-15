package ru.yandex.practicum.filmorate.exception.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;

public class FilmDurationValidationException extends ValidationException {
    public FilmDurationValidationException() {
        super();
    }

    public FilmDurationValidationException(String message) {
        super(message);
    }
}
