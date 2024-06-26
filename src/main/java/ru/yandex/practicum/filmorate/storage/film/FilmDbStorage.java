package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenresStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

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

        log.info("Занесен фильм с id: {}", filmId);

        List<Genre> genres = film.getGenres();

        if (!genres.isEmpty())
            genres.forEach(genre -> genresStorage.addFilmGenre(filmId, genre.getId()));

        List<Director> directors = film.getDirectors();

        if (!directors.isEmpty())
            directors.forEach(director -> directorStorage.addFilmDirector(filmId, director.getId()));

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

        int filmId = film.getId();

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
                filmId);

        genresStorage.deleteAllFilmGenres(filmId);
        HashSet<Genre> nonDuplicateGenre = new HashSet<>(film.getGenres());
        nonDuplicateGenre.forEach(genre -> genresStorage.addFilmGenre(filmId, genre.getId()));

        directorStorage.deleteAllFilmDirectors(filmId);
        HashSet<Director> nonDuplicateDirectors = new HashSet<>(film.getDirectors());
        nonDuplicateDirectors.forEach(director -> directorStorage.addFilmDirector(filmId, director.getId()));

        return getFilmById(filmId);
    }

    @Override
    public Film deleteFilmById(int id) {
        log.debug("FilmDbStorage - deleteFilm()");
        if (!containsById(id)) {
            throw new FilmNotFoundException();
        } else {
            Film film = getFilmById(id);
            String sqlRequest = "DELETE FROM film WHERE id = ?;";
            if (jdbcTemplate.update(sqlRequest, id) > 0) {
                return film;
            } else {
                throw new FilmNotFoundException();
            }
        }
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

    public List<Film> getCommonFilms(int userId, int friendId) {
        log.debug("FilmDbStorage - storage.getCommonFilms()");

        String sqlRequest = "SELECT \n" +
                "   f.id,\n" +
                "   f.name,\n" +
                "   f.description,\n" +
                "   f.release_date,\n" +
                "   f.duration,\n" +
                "   f.mpa\n" +
                "FROM film f\n" +
                "INNER JOIN film_like fl1 ON f.id = fl1.film_id\n" +
                "INNER JOIN film_like fl2 ON f.id = fl2.film_id\n" +
                "WHERE\n" +
                "   fl1.user_id = ? AND \n" +
                "   fl2.user_id = ?;";

        RowMapper<Film> filmMapper = (rs, rowNum) -> makeFilm(rs);
        return jdbcTemplate.query(sqlRequest, filmMapper, userId, friendId);
    }

    @Override
    public List<Film> getSortedDirectorFilmsBy(int directorId, String sortBy) {
        log.debug("DirectorDbStorage - getSortedDirectorFilmsBy()");

        String sqlRequest;
        RowMapper<Film> filmMapper = (rs, rowNum) -> makeFilm(rs);

        if (sortBy.equals("year")) {
            sqlRequest = "SELECT * FROM film\n" +
                    "WHERE id IN " +
                    "(SELECT film_id FROM film_director WHERE director_id = ?)\n" +
                    "ORDER BY release_date ASC;";

            return jdbcTemplate.query(sqlRequest, filmMapper, directorId);
        }

        sqlRequest = "SELECT * FROM (SELECT * FROM film\n" +
                "WHERE id IN (SELECT film_id FROM film_director WHERE director_id = ?)) AS f\n" +
                "LEFT JOIN\n" +
                "(SELECT film_id, COUNT(user_id) AS likes FROM film_like\n" +
                "WHERE film_id IN (SELECT film_id FROM film_director WHERE director_id = ?)\n" +
                "GROUP BY film_id) AS l ON f.id = l.film_id\n" +
                "ORDER BY likes DESC";

        return jdbcTemplate.query(sqlRequest, filmMapper, directorId, directorId);
    }

    @Override
    public List<Film> getTopFilmsBySubstringOnTitle(String condition) {
        log.debug("FilmDbStorage - getFilmsBySubstringOnTitle()");

        String sqlQuery = "SELECT * FROM film AS f " +
                "LEFT JOIN (SELECT film_id, COUNT(user_id) AS popularity " +
                "           FROM film_like " +
                "           GROUP BY film_id) AS fl ON fl.film_id = f.id " +
                "WHERE LOWER(f.name) LIKE LOWER(?) " +
                "ORDER BY fl.popularity DESC";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), "%" + condition.toLowerCase() + "%");
    }

    @Override
    public List<Film> getTopFilmsByCondition(String condition) {
        log.debug("FilmDbStorage - getTopFilmsByCondition()");

        String sqlQuery = "SELECT * FROM film AS f " +
                "LEFT JOIN film_director AS fd ON f.id = fd.film_id " +
                "LEFT JOIN director AS d ON fd.director_id = d.id " +
                "LEFT JOIN (SELECT film_id, COUNT(user_id) AS popularity " +
                "           FROM film_like " +
                "           GROUP BY film_id) AS fl " +
                "ON fl.film_id = f.id " +
                "WHERE LOWER(f.name) LIKE LOWER(?) OR LOWER(d.name) LIKE LOWER(?) " +
                "ORDER BY popularity DESC";

        String substring = "%" + condition.toLowerCase() + "%";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), substring, substring);
    }

    @Override
    public List<Film> getTopFilmsByDirector(List<Integer> directors) {
        log.debug("FilmDbStorage - getFilmsByDirector()");

        String placeholders = String.join(", ", Collections.nCopies(directors.size(), "?"));

        String sqlQuery = String.format("SELECT f.* FROM film AS f " +
                "LEFT JOIN (SELECT film_id, COUNT(user_id) AS popularity " +
                "           FROM film_Like " +
                "           GROUP BY film_id) AS fl ON fl.film_id = f.id " +
                "INNER JOIN (SELECT film_id " +
                "            FROM film_director " +
                "            WHERE director_id IN (%s)) AS fd ON f.id = fd.film_id " +
                "ORDER BY fl.popularity DESC", placeholders);

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), directors.toArray());
    }

    @Override
    public List<Film> getTopFilmsByYearAndGenre(int count, int genreId, int year) {

        String sqlQuery = "SELECT f.* FROM film AS f " +
                "INNER JOIN film_genre AS fg ON f.id = fg.film_id " +
                "LEFT JOIN (SELECT film_id, COUNT(user_id) AS like_mark " +
                "           FROM film_like fl " +
                "           GROUP BY film_id) AS mark ON f.id = mark.film_id " +
                "WHERE EXTRACT(YEAR FROM release_date) = ? " +
                "AND fg.genre_id = ? " +
                "ORDER BY mark.like_mark DESC NULLS LAST " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), year, genreId, count);
    }

    @Override
    public List<Film> getTopFilmsByYear(int count, int year) {

        String sqlQuery = "SELECT f.* FROM film AS f " +
                "LEFT JOIN (SELECT film_id, COUNT(user_id) AS like_mark " +
                "           FROM film_like fl " +
                "           GROUP BY film_id) AS mark ON f.id = mark.film_id " +
                "WHERE EXTRACT(YEAR FROM release_date) = ? " +
                "ORDER BY mark.like_mark DESC NULLS LAST " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), year, count);
    }

    @Override
    public List<Film> getTopFilmsByGenre(int count, int genreId) {

        String sqlQuery = "SELECT f.* FROM film AS f " +
                "INNER JOIN film_genre AS fg ON f.id = fg.film_id " +
                "LEFT JOIN (SELECT film_id, COUNT(user_id) AS like_mark " +
                "           FROM film_like fl " +
                "           GROUP BY film_id) AS mark ON f.id = mark.film_id " +
                "WHERE fg.genre_id = ? " +
                "ORDER BY mark.like_mark DESC NULLS LAST " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), genreId, count);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        log.debug("FilmDbStorage - makeFilm()");

        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Duration duration = Duration.ofSeconds(rs.getInt("duration"));
        Mpa mpa = mpaStorage.getFilmMpa(id);
        List<Genre> genres = genresStorage.getFilmGenre(id);
        List<Director> directors = directorStorage.getFilmsDirector(id);

        return new Film(id, name, description, releaseDate, duration, mpa, genres, directors);
    }

    @Override
    public List<Film> getSelectedFilms(List<Integer> ids) {
        log.debug("FilmDbStorage - getSelectedFilms(List<Integer> ids)");
        String sqlParam = ids.get(0).toString();
        if (ids.size() > 1) {
            for (int i = 1; i < ids.size(); i++) {
                sqlParam = sqlParam + ", " + ids.get(i).toString();
            }
        }

        String sqlRequest = "SELECT * FROM film WHERE id IN (" + sqlParam + ");";
        RowMapper<Film> filmMapper = (rs, rowNum) -> makeFilm(rs);


        return jdbcTemplate.query(sqlRequest, filmMapper);
    }
}