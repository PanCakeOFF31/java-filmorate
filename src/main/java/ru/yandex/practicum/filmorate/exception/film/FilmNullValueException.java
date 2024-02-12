package ru.yandex.practicum.filmorate.exception.film;

public class FilmNullValueException extends RuntimeException {
    public FilmNullValueException() {
        super();
    }

    public FilmNullValueException(String message) {
        super(message);
    }
}
