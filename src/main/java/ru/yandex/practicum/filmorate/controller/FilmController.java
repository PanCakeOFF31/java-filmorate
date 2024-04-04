package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.MethodNotImplemented;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;
    private final EventService eventService;
    private final DirectorService directorService;

    @GetMapping()
    public List<Film> receiveFilms(@RequestParam(defaultValue = "10") int count) {
        log.debug("/films - GET: getFilms()");
        return service.receiveFilms(count);
    }

    @GetMapping(value = "/{id}")
    public Film receiveFilmById(@PathVariable(name = "id") int filmId) {
        log.debug("/films/{} - GET: receiveFilmById", filmId);
        return service.receiveFilmById(filmId);
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
    public Film like(@PathVariable(name = "id") int filmId, @PathVariable int userId) {
        log.debug("/films/{id}/like/{userId} - PUT: like()");

        Film film = service.like(filmId, userId);

        Event event = eventService.addEvent(userId, filmId, EventType.LIKE, Operation.ADD);
        log.info("Событие лайк к фильму зарегистрировано: {}", event);

        return film;
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public Film unlike(@PathVariable(name = "id") int filmId, @PathVariable(name = "userId") int userId) {
        log.debug("/films/{id}/like/{userId} - DELETE: unlike()");

        Film film = service.unlike(filmId, userId);

        Event event = eventService.addEvent(userId, filmId, EventType.LIKE, Operation.REMOVE);
        log.info("Событие лайк к фильму зарегистрировано: {}", event);

        return film;
    }

    @GetMapping(value = "/popular")
    public List<Film> getTop(@RequestParam(name = "count", required = false, defaultValue = "10") int count) {
        log.debug("/films/popular - GET: getTop()");
        return service.getTop(count);
    }

    //    Здесь конфликт, при начале работ раскомментировать и удалить метод сверху
    // TODO: Вывод самых популярных фильмов по жанру и годам 2 SP. Реализовать функциональность.
//    @GetMapping(value = "/popular")
//    public Film getFilmByGenreYear(@RequestParam(name = "limit") final int count,
//                                   @RequestParam final int genreId,
//                                   @RequestParam final int year) {
//        log.debug("/films/popular?count={}&genreId={}&year={} - GET: getFilteredTopFilmByGenreYear", count, genreId, year);
//        throw new MethodNotImplemented("Метод получения списка самых популярных фильмов указанного жанра за нужный год");
//    }

    @DeleteMapping(value = "/{id}")
    public Film deleteFilmById(@PathVariable(name = "id") int filmId) {
        log.debug("/films/{} - DELETE: deleteFilmById", filmId);
        return service.deleteFilmById(filmId);
    }

    @GetMapping(value = "/director/{directorId}")
    public List<Film> receiveSortedDirectorFilmsDBy(@PathVariable int directorId, @RequestParam(defaultValue = "year") String sortBy) {
        log.debug("/films/director/{}?sortBy={} - GET: receiveSortedDirectorFilmsDBy", directorId, sortBy);
        return service.receiveSortedDirectorFilmsBy(directorId, sortBy);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam final int userId,
                                     @RequestParam final int friendId) {
        log.debug("/films/common?userId={}&friendId={} - GET: getCommonFilms()", userId, friendId);
        return service.getCommonFilms(userId, friendId);
    }

    // TODO: Функциональность «Поиск». 3SP. Реализовать функциональность.
    @GetMapping("/search")
    public List<Film> searchFilmsBySubstring(@RequestParam final String query,
                                             @RequestParam final List<String> by) {
        log.debug("/films/search?query={}&by={} - GET: searchFilmBySubstring()", query, by);
        return service.searchFilmByCondition(query, by);
    }
}