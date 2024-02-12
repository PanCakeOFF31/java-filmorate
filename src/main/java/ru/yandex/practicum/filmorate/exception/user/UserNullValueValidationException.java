package ru.yandex.practicum.filmorate.exception.user;

import ru.yandex.practicum.filmorate.exception.ValidationException;

public class UserNullValueValidationException extends ValidationException {
    public UserNullValueValidationException(String message) {
        super(message);
    }

    public UserNullValueValidationException() {
        super();
    }
}
