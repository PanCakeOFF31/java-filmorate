package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.MethodNotImplemented;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

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
        return service.like(filmId, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public Film unlike(@PathVariable(name = "id") int filmId, @PathVariable(name = "userId") int userId) {
        log.debug("/films/{id}/like/{userId} - DELETE: unlike()");
        return service.unlike(filmId, userId);
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

    // TODO: Удаление фильмов и пользователей 2 SP. Реализовать функциональность.
    @DeleteMapping(value = "/{id}")
    public Film deleteFilmById(@PathVariable(name = "id") int filmId) {
        log.debug("/films/{} - DELETE: deleteFilmById", filmId);
        throw new MethodNotImplemented("Метод удаления фильмов по идентификатору");
    }

    // TODO: Добавление режиссёров в фильм 4 SP. Реализовать функциональность.
    @GetMapping(value = "/director/{directorId}")
    public List<Film> receiveSortedDirectorFilmsDBy(@PathVariable int directorId, @RequestParam(defaultValue = "year") String sortBy) {
        log.debug("/films/director/{}?sortBy={} - GET: getSortedDirectorFilmsDBy", directorId, sortBy);
        throw new MethodNotImplemented("Метод получения отсортированного списка фильмов режиссера с сортировкой");
    }

    // TODO: Функциональность «Общие фильмы». 1 SP. Реализовать функциональность.
    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam final int userId,
                                     @RequestParam final int friendId) {
        log.debug("/films/common?userId={}&friendId={} - GET: getCommonFilms()", userId, friendId);
        throw new MethodNotImplemented("Метод получения списка общих с другом фильмов");
    }

    // TODO: Функциональность «Поиск». 3SP. Реализовать функциональность.
    @GetMapping("/search")
    public List<Film> searchFilmsBySubstring(@RequestParam final String query,
                                             @RequestParam final List<String> by) {
        log.debug("/films/search?query={}&by={} - GET: searchFilmBySubstring()", query, by);
        throw new MethodNotImplemented("Метод получения списка фильмов с поиском по подстроке в режиссерах/фильмах");
    }
}