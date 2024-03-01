package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.users.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@JdbcTest
@Sql(scripts = "file:./src/main/resources/schema.sql", executionPhase = BEFORE_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private FriendshipStorage friendshipStorage;

    @BeforeEach
    public void beforeEach() {
        friendshipStorage = new FriendshipDbStorage(jdbcTemplate);
    }

    @Test
    public void testFindUserById() {
        User newUser = new User(1, "user@email.ru", "vanya123", LocalDate.of(1990, 1, 1), "Ivan Petrov", new HashSet<>());
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate, friendshipStorage);
        userStorage.addUser(newUser);

        System.out.println(userStorage.getUsersQuantity());
        System.out.println(userStorage.getUsers());

        User savedUser = userStorage.getUserById(1);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testFindUserById_1() {
        User newUser = new User(1, "user@email.ru", "vanya123", LocalDate.of(1990, 1, 1), "Ivan Petrov", new HashSet<>());
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate, friendshipStorage);
        userStorage.addUser(newUser);

        System.out.println(userStorage.getUsersQuantity());
        System.out.println(userStorage.getUsers());

        User savedUser = userStorage.getUserById(1);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }
}
