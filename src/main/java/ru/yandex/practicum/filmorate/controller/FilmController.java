package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @Autowired
    public FilmController(final FilmService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Film> receiveFilms(@RequestParam(defaultValue = "10") int count) {
        log.debug("/films - GET: getFilms()");
        return service.receiveFilms(count);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody final Film film) {
        log.debug("/films - POST: addFilm()");
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody final Film film) {
        log.debug("/films - PUT: updateFilm()");
        return service.updateFilm(film);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public Film like(@PathVariable(name = "id") int filmId,
                     @PathVariable int userId) {
        log.debug("/films/{id}/like/{userId} - PUT: like()");
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Метод /feed ещё не реализован.");
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public Film unlike(@PathVariable(name = "id") int filmId,
                       @PathVariable(name = "userId") int userId) {
        log.debug("/films/{id}/like/{userId} - DELETE: unlike()");
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Метод /feed ещё не реализован.");
    }

    @GetMapping(value = "/popular")
    public List<Film> getTop(@RequestParam(name = "count", defaultValue = "10") int count) {
        log.debug("/films/popular - GET: getTop()");
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Метод /feed ещё не реализован.");
    }
}



















