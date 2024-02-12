package ru.yandex.practicum.filmorate.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@Slf4j
@RestControllerAdvice(assignableTypes = UserController.class)
public class UserControllerAdvice {

    //    На тот случай, если где-то забуду реализовать @ExceptionHandler для ValidationException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSkippedException(final ValidationException exception) {
        log.debug("UserControllerAdvice - handleSkippedExceptionHandler()");
        return new ErrorResponse("ValidationException", "Пропущен обработчик исключений валидации ValidationException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(final RuntimeException exception) {
        log.debug("ControllerAdvice - handle()");
        return new ErrorResponse("RuntimeException", "");
    }
}
