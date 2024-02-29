package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService service;

    @GetMapping()
    public Set<Mpa> getRating() {
        log.debug("/mpa - GET: getRating()");
        return service.getRatings();
    }

    @GetMapping(value = "/{id}")
    public Set<Mpa> getFilmRating(@PathVariable(name = "id") int filmId) {
        log.debug("/mpa/{id} - GET: getFilmRating()");
        return service.getFilmRating(filmId);
    }


}
