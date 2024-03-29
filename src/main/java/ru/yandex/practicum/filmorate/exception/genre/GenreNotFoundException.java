package ru.yandex.practicum.filmorate.exception.genre;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

public class GenreNotFoundException extends NotFoundException {
    public GenreNotFoundException(String message) {
        super(message);
    }

    public GenreNotFoundException() {
        super();
    }
}
