package ru.yandex.practicum.filmorate.storage.genres;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenreId;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
    public List<Genre> getAllGenres() {
        log.debug("GenreDbStorage - storage.getAllGenres()");

        String sqlRequest = "SELECT id, name FROM genres ORDER BY id ASC;";
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
                "genres AS g ON f.genre_id = g.id\n";
        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeGenre(rs), filmId);
    }

    @Override
    public List<GenreId> getFilmGenreId(int filmId) {
        log.debug("GenreDbStorage - storage.getFilmGenreId()");

        String sqlRequest = "SELECT g.id FROM\n" +
                "(SELECT *\n" +
                "FROM film_genre\n" +
                "WHERE film_id = ?) AS f\n" +
                "JOIN\n" +
                "genres AS g ON f.genre_id = g.id\n";

        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeGenreId(rs), filmId);
    }

    public Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }

    public GenreId makeGenreId(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        return new GenreId(id);
    }

}
