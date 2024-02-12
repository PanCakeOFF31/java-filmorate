package ru.yandex.practicum.filmorate.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice("ru.yandex.practicum.filmorate")
@Slf4j
public class GeneralControllerAdvice {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRunTimeException(final RuntimeException exception) {
        log.debug("GeneralControllerAdvice - handleRunTimeException()");
        log.warn(exception.getClass().toString());
        return new ErrorResponse("RuntimeException", "Не предвиденная ошибка, которую не предвидели.");
    }
}
