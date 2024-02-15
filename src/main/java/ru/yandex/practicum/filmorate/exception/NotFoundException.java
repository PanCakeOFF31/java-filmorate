package ru.yandex.practicum.filmorate.exception;

public abstract class NotFoundException extends RuntimeException {
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
