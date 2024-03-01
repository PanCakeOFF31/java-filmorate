package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.films.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.films.FilmStorage;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.genres.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genres.GenresStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikeStorage;
import ru.yandex.practicum.filmorate.storage.ratings.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.ratings.MpaStorage;
import ru.yandex.practicum.filmorate.storage.users.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.users.UserStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@JdbcTest
@Sql(scripts = "file:./src/main/resources/schema.sql", executionPhase = BEFORE_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikesDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private LikeStorage likeStorage;
    private UserStorage userStorage;
    private FilmStorage filmStorage;

    @BeforeEach
    public void beforeEach() {
        FriendshipStorage friendshipStorage = new FriendshipDbStorage(jdbcTemplate);
        likeStorage = new LikeDbStorage(jdbcTemplate);
        userStorage = new UserDbStorage(jdbcTemplate, friendshipStorage);
        GenresStorage genresStorage = new GenreDbStorage(jdbcTemplate);
        MpaStorage mpaStorage = new MpaDbStorage(jdbcTemplate);
        filmStorage = new FilmDbStorage(jdbcTemplate, likeStorage, genresStorage, mpaStorage);
    }

    @Test
    void test_T0020_PS01_like() {
        User user1 = new User(1,
                "user1@email.ru",
                "vanya123",
                LocalDate.of(1995, 1, 1),
                "Ivan Petrov",
                new HashSet<>());

        User user2 = new User(2,
                "user2@email.ru",
                "maxim123",
                LocalDate.of(1990, 1, 1),
                "Maxim Ivanov",
                new HashSet<>());

        Film film = new Film(1,
                "Болотная чешуя",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(150),
                0,
                new Mpa(1, "G"),
                new ArrayList<>());

        Integer user1Id = userStorage.addUser(user1);
        Integer user2Id = userStorage.addUser(user2);
        Integer filmId = filmStorage.addFilm(film);

        likeStorage.like(filmId, user1Id);
        likeStorage.like(filmId, user2Id);

        assertEquals(2, likeStorage.getLikes(filmId));
    }

    @Test
    void test_T0020_NS01_like_incorrectId() {
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> likeStorage.like(9999, 9999));
    }

    @Test
    void test_T0030_PS01_unlike() {
        User user1 = new User(1,
                "user1@email.ru",
                "vanya123",
                LocalDate.of(1995, 1, 1),
                "Ivan Petrov",
                new HashSet<>());

        User user2 = new User(2,
                "user2@email.ru",
                "maxim123",
                LocalDate.of(1990, 1, 1),
                "Maxim Ivanov",
                new HashSet<>());

        Film film = new Film(1,
                "Болотная чешуя",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(150),
                0,
                new Mpa(1, "G"),
                new ArrayList<>());

        Integer user1Id = userStorage.addUser(user1);
        Integer user2Id = userStorage.addUser(user2);
        Integer filmId = filmStorage.addFilm(film);

        likeStorage.like(filmId, user1Id);
        likeStorage.like(filmId, user2Id);
        likeStorage.unlike(filmId, user1Id);

        assertEquals(1, likeStorage.getLikes(filmId));
    }

    @Test
    void test_T0030_NS01_unlike_incorrectId() {
        assertFalse(likeStorage.unlike(9999, 9999));
    }
}