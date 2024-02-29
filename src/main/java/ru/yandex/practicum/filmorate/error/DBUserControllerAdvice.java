package ru.yandex.practicum.filmorate.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

@RestControllerAdvice(assignableTypes = UserDbStorage.class)
@Slf4j
public class DBUserControllerAdvice {
    private static final String CLASS_NAME = "DBUserControllerAdvice ";

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
    public ErrorResponse handleUserDuplicateKeyException(final DuplicateKeyException exception) {
        log.debug(CLASS_NAME + "- handleUserDuplicateKeyException");
        return new ErrorResponse("Ошибка добавления пользователя в таблицу",
                "Запись не проходит ограничение уникальности для поля email и login");
    }
}
