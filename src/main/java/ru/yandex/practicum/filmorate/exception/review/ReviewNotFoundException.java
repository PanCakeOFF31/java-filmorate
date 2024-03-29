package ru.yandex.practicum.filmorate.exception.review;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

public class ReviewNotFoundException extends NotFoundException {
    public ReviewNotFoundException() {
        super();
    }

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
