package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addToFriend(int userId, int friendId) {
        log.debug("FriendshipDbStorage - storage.addToFriend()");

        String sqlRequest = "INSERT INTO friendship (user_id, friend_id) VALUES (?, ?)";
        int added = jdbcTemplate.update(sqlRequest, userId, friendId);

        return added > 0;
    }

    @Override
    public boolean deleteFromFriend(int userId, int friendId) {
        log.debug("FriendshipDbStorage - storage.deleteFromFriend()");

        String sqlRequest = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?;";
        int deleted = jdbcTemplate.update(sqlRequest, userId, friendId);

        return deleted > 0;
    }

    @Override
    public int getUserFriendCounts(int userId) {
        log.debug("FriendshipDao - getUserFriendCounts()");

        String sqlRequest = "SELECT COUNT(friend_id) AS friend_count FROM friendship WHERE user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlRequest, userId);

        if (rowSet.next())
            return rowSet.getInt("friend_count");

        return 0;
    }

    public List<Integer> getUserFriendsAsId(int userId) {
        log.debug("FriendshipDao - getUserFriendsId()");

        String sqlRequest = "SELECT friend_id FROM friendship WHERE user_id = ?";
        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> rs.getInt("friend_id"), userId);
    }

    @Override
    public List<User> getUserFriendsAsUsers(int userId) {
        log.debug("FriendshipDao - getUserFriends()");

        String sqlRequest = "SELECT id, email, login, name, birthday \n" +
                "FROM users\n" +
                "INNER JOIN \n" +
                "(SELECT friend_id FROM friendship WHERE user_id = ?)\n" +
                "ON id = friend_id";

        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeUser(rs), userId);
    }

    @Override
    public List<User> getCommonFriendsAsUsers(int userId, int otherUserId) {
        log.debug("FriendshipDao - getCommonFriends()");

        String sqlRequest = "SELECT * \n" +
                "FROM users \n" +
                "WHERE id IN (\n" +
                "SELECT friend_id\n" +
                "FROM friendship\n" +
                "WHERE user_id = ?\n" +
                "INTERSECT \n" +
                "SELECT friend_id\n" +
                "FROM friendship\n" +
                "WHERE user_id = ?)";

        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeUser(rs), userId, otherUserId);
    }

    //    TODO: что-то с этим нужно сделать
    // Этот метод дублирует метод в UserDbStorage
    public User makeUser(ResultSet rs) throws SQLException {
        log.debug("UserDbStorage - makeUser()");

        int id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        List<Integer> friends = getUserFriendsAsId(id);

        return new User(id, email, login, birthday, name, new HashSet<>(friends));
    }
}
