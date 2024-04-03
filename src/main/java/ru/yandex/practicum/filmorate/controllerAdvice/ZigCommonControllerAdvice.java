package ru.yandex.practicum.filmorate.controllerAdvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.InvalidRequestParameterValue;
import ru.yandex.practicum.filmorate.exception.MethodNotImplemented;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.director.DirectorNotFounException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.mpa.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.review.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

import java.sql.SQLException;

@RestControllerAdvice(basePackages = "ru.yandex.practicum.filmorate")
@Slf4j
public class ZigCommonControllerAdvice {
    private static final String CLASS_NAME = "CommonControllerAdvice ";

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRunTimeException(final RuntimeException exception) {
        log.debug(CLASS_NAME + "- handleRunTimeException()");
        log.warn(exception.getClass().toString());
        return new ErrorResponse("RuntimeException",
                "Не предвиденная ошибка, которую не предвидели.",
                exception.getClass().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSQLException(final SQLException exception) {
        log.debug(CLASS_NAME + "- handleRunTimeException()");
        log.warn(exception.getClass().toString());
        return new ErrorResponse("RuntimeException",
                "Не предвиденная SQL ошибка, которую не предвидели.",
                exception.getClass().toString());
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
                "Пользователь с указанным идентификатором отсутствует",
                exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleMpaNotFoundException(final MpaNotFoundException exception) {
        log.debug(CLASS_NAME + "MpaNotFoundException");
        return new ErrorResponse("Ошибка существования mpa",
                "Mpa с указанным идентификатором отсутствует",
                exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleGenreNotFoundException(final GenreNotFoundException exception) {
        log.debug(CLASS_NAME + "GenreNotFoundException");
        return new ErrorResponse("Ошибка существования mpa",
                "Genre с указанным идентификатором отсутствует",
                exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDirectorNotFounException(final DirectorNotFounException exception) {
        log.debug(CLASS_NAME + "handleDirectorNotFounException");
        return new ErrorResponse("Ошибка существования director",
                "Director с указанным идентификатором отсутствует",
                exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleReviewNotFoundException(final ReviewNotFoundException exception) {
        log.debug(CLASS_NAME + "handleReviewNotFoundException");
        return new ErrorResponse("Ошибка существования review",
                "Review с указанным идентификатором отсутствует",
                exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        log.debug(CLASS_NAME + "handleMethodArgumentNotValidException");

        return new ErrorResponse("Ошибка валидации тела SQL запроса",
                "Проблемы связаны с несоблюдением ограничение объекта в теле запроса", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDuplicateKeyException(final DuplicateKeyException exception) {
        log.debug(CLASS_NAME + "handleDuplicateKeyException");

        return new ErrorResponse("Ошибка выполнения SQL запроса",
                "Проблемы связаны с нарушением уникальности данных или первичным ключом. Возможно вы пытаетесь повторно добавить существующую запись.", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException exception) {
        log.debug(CLASS_NAME + "handleDataIntegrityViolationException");

        return new ErrorResponse("Ошибка выполнения SQL запроса",
                "Проблемы связаны с нарушением целостности данных. Возможно вы пытаетесь связать таблицы по несуществующей записи.", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEmptyResultDataAccessException(final EmptyResultDataAccessException exception) {
        log.debug(CLASS_NAME + "handleEmptyResultDataAccessException");

        return new ErrorResponse("Ошибка выполнения SQL запроса",
                "Проблемы связаны с пустым результатом запроса.", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadSqlGrammarException(final BadSqlGrammarException exception) {
        log.debug(CLASS_NAME + "handleBadSqlGrammarException");

        return new ErrorResponse("Ошибка выполнения SQL запроса",
                "Проблемы с синтаксической ошибкой в запросе.", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public ErrorResponse handleMethodNotImplemented(final MethodNotImplemented exception) {
        log.debug(CLASS_NAME + "handleMethodNotImplemented");

        return new ErrorResponse("Ошибка выполнения запроса",
                "Проблемы реализацией endpoint, ", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidRequestParameterValue(final InvalidRequestParameterValue exception) {
        log.debug(CLASS_NAME + "handleInvalidRequestParameterValue");

        return new ErrorResponse("Ошибка выполнения запроса",
                "Проблемы с корректностью допустимых значений параметров запроса, ", exception.getMessage());
    }
}