package ru.yandex.practicum.filmorate.exception.review;

import ru.yandex.practicum.filmorate.exception.ValidationException;

public class ReviewUpdateValidationException extends ValidationException {
    public ReviewUpdateValidationException(String message) {
        super(message);
    }

    public ReviewUpdateValidationException() {
        super();
    }
}
