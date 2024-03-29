package ru.yandex.practicum.filmorate.storage.films;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genres.GenresStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikeStorage;
import ru.yandex.practicum.filmorate.storage.ratings.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final LikeStorage likeStorage;
    private final GenresStorage genresStorage;
    private final MpaStorage mpaStorage;

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

    public List<Film> getFilms(int count) {
        log.debug("FilmDbStorage - getFilms(int count)");

        String sqlRequest = "SELECT * FROM films LIMIT ?;";
        RowMapper<Film> userMapper = (rs, rowNum) -> makeFilm(rs);

        return jdbcTemplate.query(sqlRequest, userMapper, count);
    }

    @Override
    public Film getFilmById(int id) {
        log.debug("FilmDbStorage - getFilmById()");

        String sqlRequest = "SELECT * FROM films WHERE id = ?";
        RowMapper<Film> filmMapper = (rs, rowNum) -> makeFilm(rs);

        return jdbcTemplate.queryForObject(sqlRequest, filmMapper, id);
    }

    @Override
    public Integer addFilm(Film film) {
        log.debug("FilmDbStorage - addFilm()");

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();

        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("release_date", film.getReleaseDate());
        params.put("duration", film.getDuration());
        params.put("mpa", film.getMpa().getId());

        int filmId = jdbcInsert.executeAndReturnKey(params).intValue();
        film.setId(filmId);

        List<Genre> genres = film.getGenres();
        log.info("Занесен фильм с id: {}", filmId);

        if (!genres.isEmpty())
            genres.forEach(genre -> genresStorage.addFilmGenre(filmId, genre.getId()));

        return filmId;
    }

    @Override
    public boolean containsById(int id) {
        log.debug("FilmDbStorage - containsById()");

        String sqlRequest = "SELECT * FROM films WHERE id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        return rowSet.next();
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("FilmDbStorage - updateFilm()");

        String sqlRequest = "UPDATE films SET\n" +
                "name = ?,\n" +
                "description = ?,\n" +
                "release_date = ?,\n" +
                "duration = ?,\n" +
                "mpa = ?\n" +
                "WHERE id = ?;";

        jdbcTemplate.update(sqlRequest,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        genresStorage.deleteAllFilmGenres(film.getId());
        HashSet<Genre> nonDuplicate = new HashSet<>(film.getGenres());
        nonDuplicate.forEach(genre -> genresStorage.addFilmGenre(film.getId(), genre.getId()));

        return getFilmById(film.getId());
    }

    public List<Film> getTopFilms(int size) {
        log.debug("FilmDbStorage - storage.getTopFilms()");

        String sqlRequest = "SELECT *\n" +
                "FROM films\n" +
                "WHERE id IN (\n" +
                "   SELECT f.id\n" +
                "   FROM films AS f\n" +
                "   LEFT JOIN likes AS l ON f.id = l.film_id \n" +
                "   GROUP BY f.id\n" +
                "   ORDER BY COUNT(l.user_id) DESC)\n" +
                "LIMIT ?";

        RowMapper<Film> filmMapper = (rs, rowNum) -> makeFilm(rs);

        return jdbcTemplate.query(sqlRequest, filmMapper, size);
    }

    public Film makeFilm(ResultSet rs) throws SQLException {
        log.debug("FilmDbStorage - makeFilm()");

        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Duration duration = Duration.ofSeconds(rs.getInt("duration"));
        Mpa mpa = mpaStorage.getFilmMpa(id);
        List<Genre> genres = genresStorage.getFilmGenre(id);
        int rate = likeStorage.getLikes(id);

        return new Film(id, name, description, releaseDate, duration, rate, mpa, genres);
    }
}