package ru.yandex.practicum.filmorate.exception;

public class DataNotFoundDbException extends NotFoundException {
    public DataNotFoundDbException(String message) {
        super(message);
    }

    public DataNotFoundDbException() {
        super();
    }
}
