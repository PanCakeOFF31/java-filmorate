package ru.yandex.practicum.filmorate.storage.ratings;

import  lombok.RequiredArgsConstructor;
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

        String sqlRequest = "SELECT * FROM ratings ORDER BY id ASC;";

        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Mpa getMpa(int mpaId) {
        log.debug("MpaDbStorage - storage.getMpa()");

        String sqlRequest = "SELECT * FROM ratings WHERE id = ?;";

        return jdbcTemplate.queryForObject(sqlRequest, (rs, rowNum) -> makeMpa(rs), mpaId);
    }

    @Override
    public Mpa getFilmMpa(int filmId) {
        log.debug("MpaDbStorage - storage.getFilmRating()");

        String sqlRequest = "SELECT r.id, r.name FROM\n" +
                "(SELECT * FROM films WHERE id = ?) AS f\n" +
                "INNER JOIN \n" +
                "ratings AS r ON f.mpa = r.id";

        return jdbcTemplate.queryForObject(sqlRequest, (rs, rowNum) -> makeMpa(rs), filmId);
    }

    public Mpa makeMpa(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Mpa(id, name);
    }

    @Override
    public boolean containsById(int mpaId) {
        log.debug("MpaDbStorage - storage.containsById()");

        String sqlRequest = "SELECT * FROM ratings WHERE id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest, mpaId);

        return rowSet.next();
    }
}