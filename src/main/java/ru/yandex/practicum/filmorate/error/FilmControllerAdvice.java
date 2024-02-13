package ru.yandex.practicum.filmorate.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmDurationValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmNullValueValidationException;
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
        return new ErrorResponse("ValidationException"
                , "Пропущен обработчик исключений валидации ValidationException");
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
    public ErrorResponse handleFilmNullValueValidationException(final FilmNullValueValidationException exception) {
        log.debug(CLASS_NAME + "handleFilmNullValueValidationException");
        return new ErrorResponse("Ошибка null значение"
                , "Не должно быть указанного null значения."
                , exception.getMessage());
    }
}
