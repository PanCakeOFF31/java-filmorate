package ru.yandex.practicum.filmorate.storage.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendshipStorage friendshipDao;

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
        RowMapper<User> userMapper = (rs, rowNum) -> makeUser(rs);

        return jdbcTemplate.query(sqlRequest, userMapper);
    }

    @Override
    public List<User> getUsers(int count) {
        log.debug("UserDbStorage - storage.getUsers(int count)");

        String sqlRequest = "SELECT * FROM users LIMIT ?";
        RowMapper<User> userMapper = (rs, rowNum) -> makeUser(rs);

        return jdbcTemplate.query(sqlRequest, userMapper, count);
    }

    @Override
    public Set<Integer> getAllRowId() {
        log.debug("UserDbStorage - storage.getAllRowId()");

        String sqlRequest = "SELECT id FROM users";
        List<Integer> users = jdbcTemplate.query(sqlRequest, (rs, rowNum) -> rs.getInt("id"));

        return new HashSet<>(users);
    }

    @Override
    public User getUserById(int id) {
        log.debug("UserDbStorage - storage.getUserById()");

        String sqlRequest = "SELECT * FROM users WHERE id = ?";
        RowMapper<User> userMapper = (rs, rowNum) -> makeUser(rs);

        return jdbcTemplate.queryForObject(sqlRequest, userMapper, id);
    }

    @Override
    public Integer addUser(User user) {
        log.debug("UserDbStorage - storage.addUser()");

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();

        params.put("email", user.getEmail());
        params.put("login", user.getLogin());
        params.put("name", user.getName());
        params.put("birthday", user.getBirthday());

        int userId = jdbcInsert.executeAndReturnKey(params).intValue();
        user.setId(userId);

        log.info("Занесен пользователь с id: {}", userId);

        return userId;
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

        String sqlRequest = "UPDATE users SET\n" +
                "email = ?,\n" +
                "login = ?,\n" +
                "name = ?,\n" +
                "birthday = ?\n" +
                "WHERE id = ?;";

        jdbcTemplate.update(sqlRequest,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return getUserById(user.getId());
    }

    // Этот метод дублирует метод в FriendshipDao
    public User makeUser(ResultSet rs) throws SQLException {
        log.debug("UserDbStorage - makeUser()");

        int id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        List<Integer> friends = friendshipDao.getUserFriendsAsId(id);

        return new User(id, email, login, birthday, name, new HashSet<>(friends));
    }
}