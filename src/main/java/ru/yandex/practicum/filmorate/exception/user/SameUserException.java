package ru.yandex.practicum.filmorate.exception.user;

public class SameUserException extends RuntimeException {
    public SameUserException() {
        super();
    }

    public SameUserException(String message) {
        super(message);
    }
}
