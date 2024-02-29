package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService service;

    @GetMapping()
    public List<Genre> getGenres() {
        log.debug("/genres - GET: getGenres()");
        return service.getGenres();
    }

    @GetMapping(value = "{id}")
    public Genre getGenre(@PathVariable(name = "id") int genreId) {
        log.debug("/genres/{id} - GET: getFilmGenres()");
        return service.getGenre(genreId);
    }


}
