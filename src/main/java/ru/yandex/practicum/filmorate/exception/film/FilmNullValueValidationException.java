package ru.yandex.practicum.filmorate.exception.film;

public class FilmNullValueValidationException extends RuntimeException {
    public FilmNullValueValidationException() {
        super();
    }

    public FilmNullValueValidationException(String message) {
        super(message);
    }
}
