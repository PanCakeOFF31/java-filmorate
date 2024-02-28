package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public int getFilmsQuantity() {
        return 0;
    }

    @Override
    public List<Film> getFilms() {
        return null;
    }

    @Override
    public Set<Integer> getKeys() {
        return null;
    }

    @Override
    public Film getFilmById(int id) {
        return null;
    }

    @Override
    public Integer addFilm(Film film) {
        return null;
    }

    @Override
    public boolean containsFilm(Film film) {
        return false;
    }

    @Override
    public boolean containsById(int id) {
        return false;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }
}
