package ru.yandex.practicum.filmorate.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@Slf4j
@RestControllerAdvice(assignableTypes = FilmController.class)
public class FilmSqlControllerAdvice {
    private static final String CLASS_NAME = "FilmSqlControllerAdvice ";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        log.debug(CLASS_NAME + "handleMethodArgumentNotValidException");

        return new ErrorResponse("Ошибка валидации тела SQL запроса",
                "Проблемы связаны с несоблюдением ограничение Film объекта в теле запроса", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDuplicateKeyException(final DuplicateKeyException exception) {
        log.debug(CLASS_NAME + "handleDuplicateKeyException");

        return new ErrorResponse("Ошибка выполнения SQL запроса",
                "Проблемы связаны с нарушением уникальности данных или первичным ключом. Возможно вы пытаетесь повторно добавить фильм.", exception.getMessage());
    }
}