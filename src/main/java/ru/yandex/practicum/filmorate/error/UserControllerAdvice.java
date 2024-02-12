package ru.yandex.practicum.filmorate.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.user.UserNullValueValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@Slf4j
@RestControllerAdvice(assignableTypes = UserController.class)
public class UserControllerAdvice {
    private static final String CLASS_NAME = "UserControllerAdvice ";

    //    На тот случай, если где-то забуду реализовать @ExceptionHandler для ValidationException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSkippedValidationException(final ValidationException exception) {
        log.debug(CLASS_NAME + "handleSkippedValidationExceptionHandler()");
        return new ErrorResponse("ValidationException",
                "Пропущен обработчик исключений валидации ValidationException");
    }

    //    На тот случай, если где-то забуду реализовать @ExceptionHandler для ObjectNotFoundException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSkippedObjectNotFoundedException(final NotFoundException exception) {
        log.debug(CLASS_NAME + "handleSkippedObjectNotFoundedException");
        return new ErrorResponse("ObjectNotFoundException",
                "Пропущен обработчик исключений валидации ObjectNotFoundException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserNullValueValidationException(final UserNullValueValidationException exception) {
        log.debug(CLASS_NAME + "handleUserNullValueValidationException");
        return new ErrorResponse("Ошибка null значение"
                , "Не должно быть указанного null значения."
                , exception.getMessage());
    }

}
