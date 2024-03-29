package ru.yandex.practicum.filmorate.exception.mpa;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

public class MpaNotFoundException extends NotFoundException {
    public MpaNotFoundException() {
        super();
    }

    public MpaNotFoundException(String message) {
        super(message);
    }
}
