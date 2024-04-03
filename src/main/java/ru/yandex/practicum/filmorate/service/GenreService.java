package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenresStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenresStorage genresStorage;
    private final ExistChecker existChecker;

    public List<Genre> getGenres() {
        log.debug("GenreService - service.getGenres()");
        return genresStorage.getAllGenres();
    }

    public Genre getGenre(int genreId) {
        log.debug("GenreService - service.getGenre()");
        existChecker.genreIsExist(genreId);
        return genresStorage.getGenre(genreId);
    }
}