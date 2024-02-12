package ru.yandex.practicum.filmorate.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmDurationValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.film.FilmNullValueException;
import ru.yandex.practicum.filmorate.exception.film.FilmReleaseDateValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.restriction.FilmRestriction;

@Slf4j
@RestControllerAdvice(assignableTypes = FilmController.class)
public class FilmControllerAdvice {
    private static final String CLASS_NAME = "FilmControllerAdvice ";

    //    На тот случай, если где-то забуду реализовать @ExceptionHandler для ValidationException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSkippedValidationException(final ValidationException exception) {
        log.debug(CLASS_NAME + "handleSkippedValidationException");
        return new ErrorResponse("ValidationException", "Пропущен обработчик исключений валидации ValidationException");
    }

    //    На тот случай, если где-то забуду реализовать @ExceptionHandler для ObjectNotFoundException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSkippedObjectNotFoundedException(final NotFoundException exception) {
        log.debug(CLASS_NAME + "handleSkippedObjectNotFoundedException");
        return new ErrorResponse("ObjectNotFoundException", "Пропущен обработчик исключений валидации ObjectNotFoundException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFilmDurationValidationException(final FilmDurationValidationException exception) {
        log.debug(CLASS_NAME + "handleFilmDurationValidationException");
        return new ErrorResponse("Ошибка продолжительности фильма"
                , "Продолжительность фильма должна быть больше" + FilmRestriction.MIN_DURATION + " секунд"
                , exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFilmReleaseDateValidationException(final FilmReleaseDateValidationException exception) {
        log.debug(CLASS_NAME + "handleFilmReleaseDateValidationException");
        return new ErrorResponse("Ошибка даты выхода фильма"
                , "Допустимое значение, не раньше: " + FilmRestriction.REALEASE_DATE
                , exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFilmNullValueException(final FilmNullValueException exception) {
        log.debug(CLASS_NAME + "handleFilmNullValueException");
        return new ErrorResponse("Ошибка null значение"
                , "Не должно быть указанного null значения."
                , exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFilmNotFoundException(final FilmNotFoundException exception) {
        log.debug(CLASS_NAME + "handleFilmNotFoundException");
        return new ErrorResponse("Не найден фильм"
                , "Какое-то описание ошибки"
                , exception.getMessage());
    }


}
