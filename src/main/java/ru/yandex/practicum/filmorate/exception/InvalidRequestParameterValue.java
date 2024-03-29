package ru.yandex.practicum.filmorate.exception;

public class InvalidRequestParameterValue extends RuntimeException {
    public InvalidRequestParameterValue() {
        super();
    }

    public InvalidRequestParameterValue(String message) {
        super(message);
    }
}
