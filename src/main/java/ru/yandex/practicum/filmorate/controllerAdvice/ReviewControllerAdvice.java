package ru.yandex.practicum.filmorate.controllerAdvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.ReviewController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.review.ReviewUpdateValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@Slf4j
@RestControllerAdvice(assignableTypes = ReviewController.class)
public class ReviewControllerAdvice {
    private static final String CLASS_NAME = "ReviewControllerAdvice ";

    //    На тот случай, если где-то забуду реализовать @ExceptionHandler для ValidationException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSkippedValidationException(final ValidationException exception) {
        log.debug(CLASS_NAME + "handleSkippedValidationException");

        return new ErrorResponse("ValidationException",
                "Пропущен обработчик исключений валидации ValidationException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleReviewUpdateValidationException(final ReviewUpdateValidationException exception) {
        log.debug(CLASS_NAME + "handleReviewUpdateValidationException");

        return new ErrorResponse("Ошибка выполнения запроса",
                "Проблемы с валидацией обновления отзыва, ", exception.getMessage());
    }
}
