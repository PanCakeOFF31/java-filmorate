package ru.yandex.practicum.filmorate.exception;

public abstract class ValidationException extends RuntimeException {
    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }
}
