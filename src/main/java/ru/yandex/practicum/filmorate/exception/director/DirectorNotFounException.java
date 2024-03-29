package ru.yandex.practicum.filmorate.exception.director;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

public class DirectorNotFounException extends NotFoundException

{
    public DirectorNotFounException() {
        super();
    }

    public DirectorNotFounException(String message) {
        super(message);
    }
}
