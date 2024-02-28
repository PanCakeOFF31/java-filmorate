package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.storage.likes.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final LikeStorage likeStorage;

    @Override
    public int getFilmsQuantity() {
        log.debug("FilmDbStorage - getFilmsQuantity()");

        String sqlRequest = "SELECT COUNT(*) AS films_quantity FROM films;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest);

        if (rowSet.next())
            return rowSet.getInt("films_quantity");

        return 0;
    }

    @Override
    public List<Film> getFilms() {
        log.debug("FilmDbStorage - getFilms()");

        String sqlRequest = "SELECT * FROM films;";
        RowMapper<Film> userMapper = (rs, rowNum) -> makeFilm(rs);

        return jdbcTemplate.query(sqlRequest, userMapper);
    }

    @Override
    public Set<Integer> getAllRowId() {
        log.debug("FilmDbStorage - getAllRowId()");

        String sqlRequest = "SELECT id FROM films;";
        RowMapper<Integer> mapper = (rs, rowNum) -> rs.getInt("id");

        List<Integer> films = jdbcTemplate.query(sqlRequest, mapper);
        return new HashSet<>(films);
    }

    @Override
    public Film getFilmById(int id) {
        log.debug("FilmDbStorage - getFilmById()");

        String sqlRequest = "SELECT * FROM films WHERE id = ?;";
        return null;
    }

    @Override
    public Integer addFilm(Film film) {
        log.debug("FilmDbStorage - addFilm()");

        String sqlRequest = "INSERT INTO films (name, description, release_date, duration, rating)\n" +
                "VALUES (?, ?, ?, ?, ?);";

        jdbcTemplate.update(sqlRequest,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRating());

        return getFilmId(film);
    }

    @Override
    public boolean containsFilm(Film film) {
        log.debug("FilmDbStorage - containsFilm()");

        String sqlRequest = "";
        return false;
    }

    @Override
    public boolean containsById(int id) {
        log.debug("FilmDbStorage - containsById()");

        String sqlRequest = "";
        return false;
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("FilmDbStorage - updateFilm()");

        String sqlRequest = "";
        return null;
    }

    private int getFilmId(Film film) {
        log.debug("FilmDbStorage - storage.getFilmId()");

        String sqlRequest = "SELECT * FROM films WHERE name = ? AND description = ?";
        RowMapper<Film> filmMapper = (rs, rowNum) -> makeFilm(rs);
        Film filmFromDg = jdbcTemplate.queryForObject(sqlRequest, filmMapper, film.getName(), film.getDescription());

        return filmFromDg.getId();
    }

    public Film makeFilm(ResultSet rs) throws SQLException {
        log.debug("FilmDbStorage - makeFilm()");
//
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("releaseDate").toLocalDate();
        Duration duration = Duration.ofSeconds(rs.getInt("duration"));
        FilmRating ratings = FilmRating.valueOf(rs.getString("rating"));
        Set<Integer> likes = likeStorage.getLikes(id);
//
        return new Film(id, name, description, releaseDate, duration, ratings, likes);
    }
}
