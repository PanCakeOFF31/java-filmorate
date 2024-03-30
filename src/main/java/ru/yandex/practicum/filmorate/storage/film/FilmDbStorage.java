package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.filmDirector.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.filmGenre.GenresStorage;
import ru.yandex.practicum.filmorate.storage.filmMpa.MpaStorage;

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
    private final GenresStorage genresStorage;
    private final MpaStorage mpaStorage;
    private final DirectorStorage directorStorage;

    @Override
    public int getFilmsQuantity() {
        log.debug("FilmDbStorage - getFilmsQuantity()");

        String sqlRequest = "SELECT COUNT(*) AS films_quantity FROM film;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest);

        if (rowSet.next())
            return rowSet.getInt("films_quantity");

        return 0;
    }

    @Override
    public List<Film> getFilms() {
        log.debug("FilmDbStorage - getFilms()");

        String sqlRequest = "SELECT * FROM film;";
        RowMapper<Film> filmMapper = (rs, rowNum) -> makeFilm(rs);

        return jdbcTemplate.query(sqlRequest, filmMapper);
    }

    public List<Film> getFilms(int count) {
        log.debug("FilmDbStorage - getFilms(int count)");

        String sqlRequest = "SELECT * FROM film LIMIT ?;";
        RowMapper<Film> filmMapper = (rs, rowNum) -> makeFilm(rs);

        return jdbcTemplate.query(sqlRequest, filmMapper, count);
    }

    @Override
    public Film getFilmById(int id) {
        log.debug("FilmDbStorage - getFilmById()");

        String sqlRequest = "SELECT * FROM film WHERE id = ?";
        RowMapper<Film> filmMapper = (rs, rowNum) -> makeFilm(rs);

        return jdbcTemplate.queryForObject(sqlRequest, filmMapper, id);
    }

    @Override
    public Integer addFilm(Film film) {
        log.debug("FilmDbStorage - addFilm()");

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();

        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("release_date", film.getReleaseDate());
        params.put("duration", film.getDuration());
        params.put("mpa", film.getMpa().getId());

        int filmId = jdbcInsert.executeAndReturnKey(params).intValue();
        film.setId(filmId);

        log.info("Занесен фильм с id: {}", filmId);

        List<Genre> genres = film.getGenres();

        if (!genres.isEmpty())
            genres.forEach(genre -> genresStorage.addFilmGenre(filmId, genre.getId()));

        return filmId;
    }

    @Override
    public boolean containsById(int id) {
        log.debug("FilmDbStorage - containsById()");

        String sqlRequest = "SELECT * FROM film WHERE id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        return rowSet.next();
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("FilmDbStorage - updateFilm()");

        String sqlRequest = "UPDATE film SET\n" +
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
                "FROM film\n" +
                "WHERE id IN (\n" +
                "   SELECT f.id\n" +
                "   FROM film AS f\n" +
                "   LEFT JOIN film_like AS l ON f.id = l.film_id \n" +
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
        List<Director> directors = directorStorage.getFilmDirector(id);

        return new Film(id, name, description, releaseDate, duration, mpa, genres, directors);
    }
}