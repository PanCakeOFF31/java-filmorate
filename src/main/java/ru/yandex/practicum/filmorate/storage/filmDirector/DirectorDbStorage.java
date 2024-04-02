package ru.yandex.practicum.filmorate.storage.filmDirector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Director> getFilmsDirector(int filmId) {
        log.debug("DirectorDbStorage - getFilmsDirector()");

        String sqlRequest = "SELECT d.id, d.name FROM\n" +
                "(SELECT *\n" +
                "FROM film_director\n" +
                "WHERE film_id = ?) AS fd\n" +
                "JOIN\n" +
                "director AS d ON fd.director_id = d.id\n";

        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeDirector(rs), filmId);
    }

    @Override
    public Integer createDirector(Director director) {
        log.debug("DirectorDbStorage - createDirector()");

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("director")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", director.getName());

        int directorId = jdbcInsert.executeAndReturnKey(params).intValue();
        log.info("Занесен директор с id: {}", directorId);

        return directorId;
    }

    @Override
    public Director getDirectorById(int directorId) {
        log.debug("DirectorDbStorage - getDirectorById()");

        String sqlRequest = "SELECT * FROM director WHERE id = ?";
        RowMapper<Director> directorMapper = (rs, rowNum) -> makeDirector(rs);

        return jdbcTemplate.queryForObject(sqlRequest, directorMapper, directorId);
    }

    @Override
    public Director changeDirector(Director director) {
        log.debug("DirectorDbStorage - changeDirector()");

        int directorId = director.getId();

        String sqlRequest = "UPDATE director SET name = ? WHERE id = ?";
        jdbcTemplate.update(sqlRequest, director.getName(), directorId);

        return getDirectorById(directorId);
    }

    @Override
    public Director deleteDirectorById(int directorId) {
        log.debug("DirectorDbStorage - deleteDirectorById()");

        Director deletedDirector = getDirectorById(directorId);

        String sqlRequest = "DELETE FROM director WHERE id = ?";
        jdbcTemplate.update(sqlRequest, directorId);

        return deletedDirector;
    }

    @Override
    public List<Director> getDirectors() {
        log.debug("DirectorDbStorage - getDirectors()");

        String sqlRequest = "SELECT * FROM director;";
        RowMapper<Director> directorMapper = (rs, rowNum) -> makeDirector(rs);

        return jdbcTemplate.query(sqlRequest, directorMapper);
    }

    @Override
    public int getDirectorQuantity() {
        log.debug("DirectorDbStorage - getDirectorQuantity()");

        String sqlRequest = "SELECT COUNT(*) AS directors_quantity FROM director;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest);

        if (rowSet.next())
            return rowSet.getInt("directors_quantity");

        return 0;
    }

    @Override
    public boolean containsById(int directorId) {
        log.debug("DirectorDbStorage - containsById()");

        String sqlRequest = "SELECT * FROM director WHERE id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest, directorId);

        return rowSet.next();
    }

    @Override
    public boolean addFilmDirector(int filmId, int directorId) {
        log.debug("DirectorDbStorage - addFilmDirector()");

        String sqlRequest = "INSERT INTO film_director (film_id, director_id) VALUES (?, ?);";
        int added = jdbcTemplate.update(sqlRequest, filmId, directorId);

        return added > 0;
    }

    @Override
    public boolean deleteAllFilmDirectors(int filmId) {
        log.debug("DirectorDbStorage - deleteAllFilmDirectors()");

        String sqlRequest = "DELETE FROM film_director WHERE film_id = ?;";
        int deleted = jdbcTemplate.update(sqlRequest, filmId);

        return deleted > 0;
    }

    private Director makeDirector(ResultSet rs) throws SQLException {
        log.debug("DirectorDbStorage - makeDirector()");

        int id = rs.getInt("id");
        String name = rs.getString("name");

        return new Director(id, name);
    }

    @Override
    public List<Integer> getDirectorsIdBySubstringOnName(String condition) {
        log.debug("DirectorDbStorage - getDirectorsIdByCondition()");

        String sqlQuery = "SELECT id, name FROM director WHERE LOWER(name) LIKE LOWER(?)";

        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> rs.getInt("id"),
                "%" + condition + "%");
    }
}
