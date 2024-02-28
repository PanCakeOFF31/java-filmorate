package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public int getUsersQuantity() {
        log.debug("UserDbStorage - storage.getUsersQuantity()");

        String sqlRequest = "SELECT COUNT(*) AS users_quantity FROM users;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest);

        if (rowSet.next())
            return rowSet.getInt("users_quantity");

        return 0;
    }

    @Override
    public List<User> getUsers() {
        log.debug("UserDbStorage - storage.getUsers()");

        String sqlRequest = "SELECT * FROM users";
        RowMapper<User> userMapper = (rs, rowMapper) -> makeUser(rs);
        return jdbcTemplate.query(sqlRequest, userMapper);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(id, email, login, birthday, name, null);
    }

    @Override
    public Set<Integer> getIds() {
        return null;
    }

    @Override
    public User getUserById(int id) {
        log.debug("UserDbStorage - storage.getUserById()");

        String sqlRequest = "SELECT * FROM users WHERE id = ?";
        RowMapper<User> userMapper = (rs, rowMapper) -> makeUser(rs);
        return jdbcTemplate.queryForObject(sqlRequest, userMapper, id);
    }

    @Override
    public Integer addUser(User user) {
        log.debug("UserDbStorage - storage.addUser()");

        String sqlRequest = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?);";

        System.out.println(user);

        jdbcTemplate.update(sqlRequest,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        return null;
    }

    @Override
    public boolean containsUser(User user) {
        return containsById(user.getId());
    }

    @Override
    public boolean containsById(int id) {
        log.debug("UserDbStorage - storage.containsById()");

        String sqlRequest = "SELECT * FROM users WHERE id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);
        return rowSet.next();
    }

    @Override
    public User updateUser(User user) {
        log.debug("UserDbStorage - storage.updateUser()");

        String sqlRequest = "INSERT INTO users VALUES (?, ?, ?, ?, ?) ON CONFLICT DO UPDATE";

        return null;
    }
}
