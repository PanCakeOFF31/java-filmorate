package ru.yandex.practicum.filmorate.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice(basePackages = "ru.yandex.practicum.filmorate")
@Slf4j
public class ZigCommonControllerAdvice {
    private static final String CLASS_NAME = "GeneralControllerAdvice ";

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRunTimeException(final RuntimeException exception) {
        log.debug("GeneralControllerAdvice - handleRunTimeException()");
        log.warn(exception.getClass().toString());
        return new ErrorResponse("RuntimeException",
                "Не предвиденная ошибка, которую не предвидели.");
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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundException(final FilmNotFoundException exception) {
        log.debug(CLASS_NAME + "handleFilmNotFoundException");
        return new ErrorResponse("Ошибка существования фильма",
                "Фильм с указанным идентификатором отсутствует",
                exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException exception) {
        log.debug(CLASS_NAME + "UserNotFoundException");
        return new ErrorResponse("Ошибка существования пользователя",
                "Фильм с указанным идентификатором отсутствует",
                exception.getMessage());
    }
}