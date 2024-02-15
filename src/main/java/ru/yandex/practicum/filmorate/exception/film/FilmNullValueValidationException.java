package ru.yandex.practicum.filmorate.exception.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;

public class FilmNullValueValidationException extends ValidationException {
    public FilmNullValueValidationException() {
        super();
    }

    public FilmNullValueValidationException(String message) {
        super(message);
    }
}
