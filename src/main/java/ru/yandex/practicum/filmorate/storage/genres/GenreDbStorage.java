package ru.yandex.practicum.filmorate.storage.genres;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenreDbStorage implements GenresStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFilmGenre(int filmId, int genreId) {

    }

    @Override
    public void deleteFilmGenre(int filmId, int genreId) {

    }

    @Override
    public Set<Genre> getAllGenres() {
        return null;
    }

    @Override
    public Set<Genre> getFilmGenre(int filmId) {
        return null;
    }
}
