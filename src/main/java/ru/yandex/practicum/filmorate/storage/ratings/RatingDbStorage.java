package ru.yandex.practicum.filmorate.storage.ratings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class RatingDbStorage implements RatingStorage {
    private final JdbcTemplate jdbcTemplate;
    
    @Override
    public Set<Mpa> getAllRating() {
        log.debug("RatingDbStorage - storage.getAllRating()");


        return null;
    }

    @Override
    public Set<Mpa> getFilmRating(int filmId) {
        log.debug("RatingDbStorage - storage.getFilmRating()");

        return null;
    }
}
