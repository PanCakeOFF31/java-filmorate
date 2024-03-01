package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.film.FilmNullValueValidationException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.films.FilmStorage;
import ru.yandex.practicum.filmorate.storage.films.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.users.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.users.UserStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    private FilmService service;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private Film film;

    @BeforeEach
    public void initialize() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
//        service = new FilmService(filmStorage, userStorage);
    }

    @Test
    public void test_T0020_PS01_addValidation() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));

        boolean actual = service.addValidation(film);
        assertTrue(actual);

        assertEquals(0, filmStorage.getFilmsQuantity());

        service.addFilm(film);
        assertNotNull(film.getId());

        assertEquals(1, filmStorage.getFilmsQuantity());
    }

    @Test
    public void test_T0020_NS01_addValidation_releaseDate() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(Duration.ofMinutes(90));

        assertEquals(0, filmStorage.getFilmsQuantity());
        assertThrows(ValidationException.class, () -> service.addFilm(film));
        assertEquals(0, filmStorage.getFilmsQuantity());
    }

    @Test
    public void test_T0020_NS02_addValidation_negativeDuration() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2000, 12, 28));
        film.setDuration(Duration.ofMinutes(-90));

        assertEquals(0, filmStorage.getFilmsQuantity());
        assertThrows(ValidationException.class, () -> service.addFilm(film));
        assertEquals(0, filmStorage.getFilmsQuantity());
    }

    @Test
    public void test_T0010_PS01_updateValidation() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));

        service.addFilm(film);
        assertNotNull(film.getId());

        boolean actual = service.updateValidation(film);
        assertTrue(actual);
    }

    @Test
    public void test_T0010_NS01_updateValidation_noId() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));

        assertNull(film.getId());

        assertEquals(0, filmStorage.getFilmsQuantity());
        assertThrows(FilmNullValueValidationException.class, () -> service.updateFilm(film));
        assertEquals(0, filmStorage.getFilmsQuantity());

    }

    @Test
    public void test_T0010_NS02_updateValidation_incorrectId() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));

        film.setId(9999);

        assertEquals(0, filmStorage.getFilmsQuantity());
        assertThrows(FilmNotFoundException.class, () -> service.updateFilm(film));
        assertEquals(0, filmStorage.getFilmsQuantity());

    }

    @Test
    public void test_T0030_PS01_likeValidation() {
        assertEquals(0, filmStorage.getFilmsQuantity());

        Film film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));

        User user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        user.setFriends(new HashSet<>());

        Integer filmId = filmStorage.addFilm(film);
        Integer userId = userStorage.addUser(user);

        service.like(filmId, userId);

        assertEquals(1, filmStorage.getFilmsQuantity());
        assertEquals(1, film.getRate());
    }

    @Test
    public void test_T0030_NS01_likeValidation_noContainsFilmId() {
        assertEquals(0, filmStorage.getFilmsQuantity());

        User user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        user.setFriends(new HashSet<>());

        Integer userId = userStorage.addUser(user);

        assertThrows(FilmNotFoundException.class, () -> service.likeValidation(userId + 1, userId));
    }

    @Test
    public void test_T0030_NS02_likeValidation_noContainsUserId() {
        assertEquals(0, filmStorage.getFilmsQuantity());

        Film film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));

        service.addFilm(film);

        assertThrows(UserNotFoundException.class, () -> service.likeValidation(film.getId(), film.getId() + 1));
    }
}