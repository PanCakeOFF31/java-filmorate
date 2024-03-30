package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping
    public List<Director> receiveDirectors() {
        log.debug("/directors - GET: receiveDirectors()");
        return service.receiveDirectors();
    }

    @GetMapping("/{id}")
    public Director receiveDirectorById(@PathVariable(name = "id") final int directorId) {
        log.debug("/directors/{} - GET: receiveDirectorById()", directorId);
        return service.receiveDirectorById(directorId);
    }

    @PostMapping
    public Director createDirector(@Valid @RequestBody final Director director) {
        log.debug("/directors - Post: createDirector()");
        return service.createDirector(director);
    }

    @PutMapping
    public Director changeDirector(@Valid @RequestBody final Director director) {
        log.debug("/directors - Put: changeDirector()");
        return service.changeDirector(director);
    }

    @DeleteMapping("/{id}")
    public Director deleteDirectorById(@PathVariable(name = "id") final int directorId) {
        log.debug("/directors/{} - DELETE: deleteDirectorById()", directorId);
        return service.deleteDirectorById(directorId);
    }
}
