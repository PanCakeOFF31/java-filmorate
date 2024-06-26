package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {
        log.debug("MpaDbStorage - storage.getAllRating()");

        String sqlRequest = "SELECT * FROM mpa ORDER BY id ASC;";

        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Mpa getMpa(int mpaId) {
        log.debug("MpaDbStorage - storage.getMpa()");

        String sqlRequest = "SELECT * FROM mpa WHERE id = ?;";

        return jdbcTemplate.queryForObject(sqlRequest, (rs, rowNum) -> makeMpa(rs), mpaId);
    }

    @Override
    public Mpa getFilmMpa(int filmId) {
        log.debug("MpaDbStorage - storage.getFilmMpa()");

        String sqlRequest = "SELECT mpa.id, mpa.name\n" +
                "FROM mpa \n" +
                "INNER JOIN film ON mpa.id = film.mpa\n" +
                "WHERE film.id = ?";

        return jdbcTemplate.queryForObject(sqlRequest, (rs, rowNum) -> makeMpa(rs), filmId);
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Mpa(id, name);
    }

    @Override
    public boolean containsById(int mpaId) {
        log.debug("MpaDbStorage - storage.containsById()");

        String sqlRequest = "SELECT * FROM mpa WHERE id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest, mpaId);

        return rowSet.next();
    }
}