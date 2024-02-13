package ru.yandex.practicum.filmorate.exception.user;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
