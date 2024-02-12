package ru.yandex.practicum.filmorate.exception.user;

public class UserNullValueException extends RuntimeException {
    public UserNullValueException(String message) {
        super(message);
    }

    public UserNullValueException() {
        super();
    }
}
