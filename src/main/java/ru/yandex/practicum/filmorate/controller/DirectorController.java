package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.MethodNotImplemented;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService service;

    // TODO: Добавление режиссёров в фильм 4 SP. Реализовать функциональность.
    @GetMapping
    public List<Director> receiveDirectors() {
        log.debug("/directors - GET: receiveDirector()");
        throw new MethodNotImplemented("Метод по получению всех режиссеров еще не реализован");
    }

    // TODO: Добавление режиссёров в фильм 4 SP. Реализовать функциональность.
    @GetMapping("/{id}")
    public List<Director> receiveDirectorById(@PathVariable(name = "id") final int directorId) {
        log.debug("/directors/{} - GET: receiveDirectorById()", directorId);
        throw new MethodNotImplemented("Метод по получения режиссера по идентификатору еще не реализован");
    }

    // TODO: Добавление режиссёров в фильм 4 SP. Реализовать функциональность.
    @PostMapping
    public Director createDirector(@Valid @RequestBody final Director director) {
        log.debug("/directors - Post: createDirector()");
        throw new MethodNotImplemented("Метод по созданию режиссера еще не реализован");
    }

    // TODO: Добавление режиссёров в фильм 4 SP. Реализовать функциональность.
    @PutMapping
    public Director changeDirector(@Valid @RequestBody final Director director) {
        log.debug("/directors - Put: changeDirector()");
        throw new MethodNotImplemented("Метод по изменению режиссера еще не реализован");
    }

    // TODO: Добавление режиссёров в фильм 4 SP. Реализовать функциональность.
    @DeleteMapping("/{id}")
    public Director deleteDirectorById(@PathVariable(name = "id") final int directorId) {
        log.debug("/directors/{} - DELETE: deleteDirectorById()", directorId);
        throw new MethodNotImplemented("Метод по удалению режиссера еще не реализован");
    }
}
