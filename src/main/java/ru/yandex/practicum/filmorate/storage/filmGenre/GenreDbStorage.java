package ru.yandex.practicum.filmorate.storage.filmGenre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenreDbStorage implements GenresStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addFilmGenre(int filmId, int genreId) {
        log.debug("GenreDbStorage - storage.addFilmGenre()");

        String sqlRequest = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?);";
        int added = jdbcTemplate.update(sqlRequest, filmId, genreId);

        return added > 0;
    }

    @Override
    public boolean deleteAllFilmGenres(int filmId) {
        log.debug("GenreDbStorage - storage.deleteFilmGenre()");

        String sqlRequest = "DELETE FROM film_genre WHERE film_id = ?;";
        int deleted = jdbcTemplate.update(sqlRequest, filmId);

        return deleted > 0;
    }

    @Override
    public List<Genre> getAllGenres() {
        log.debug("GenreDbStorage - storage.getAllGenres()");

        String sqlRequest = "SELECT id, name FROM genre ORDER BY id ASC;";
        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public List<Genre> getFilmGenre(int filmId) {
        log.debug("GenreDbStorage - storage.getFilmGenre()");

        String sqlRequest = "SELECT g.id, g.name FROM\n" +
                "(SELECT *\n" +
                "FROM film_genre\n" +
                "WHERE film_id = ?) AS f\n" +
                "JOIN\n" +
                "genre AS g ON f.genre_id = g.id\n";

        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeGenre(rs), filmId);
    }

    @Override
    public Genre getGenre(int genreId) {
        log.debug("GenreDbStorage - storage.getGenre()");

        String sqlRequest = "SELECT * FROM genre WHERE id = ?;";

        return jdbcTemplate.queryForObject(sqlRequest, (rs, rowNum) -> makeGenre(rs), genreId);
    }

    public Genre makeGenre(ResultSet rs) throws SQLException {
        log.debug("GenreDbStorage - storage.makeGenre()");

        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }

    @Override
    public boolean containsById(int genreId) {
        log.debug("GenreDbStorage - storage.containsById()");

        String sqlRequest = "SELECT * FROM genre WHERE id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest, genreId);

        return rowSet.next();
    }
}