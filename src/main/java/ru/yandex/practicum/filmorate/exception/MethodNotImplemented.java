package ru.yandex.practicum.filmorate.exception;

public class MethodNotImplemented extends RuntimeException {
    public MethodNotImplemented() {
    }

    public MethodNotImplemented(String message) {
        super(message);
    }
}
