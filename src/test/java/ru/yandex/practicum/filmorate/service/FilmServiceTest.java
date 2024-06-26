package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.film.FilmNullValueValidationException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmServiceTest {
    private final FilmService filmService;
    private final UserService userService;
    private final FilmStorage filmStorage;
    private Film film;

    @Test
    public void test_T0020_PS01_addValidation() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));
        film.setMpa(new Mpa(1, null));
        film.setGenres(new ArrayList<>());
        film.setDirectors(new ArrayList<>());
        film.setDirectors(new ArrayList<>());

        boolean actual = filmService.addValidation(film);
        assertTrue(actual);

        assertEquals(0, filmStorage.getFilmsQuantity());

        film = filmService.addFilm(film);
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
        film.setMpa(new Mpa(1, null));
        film.setGenres(new ArrayList<>());
        film.setDirectors(new ArrayList<>());

        assertEquals(0, filmStorage.getFilmsQuantity());
        assertThrows(ValidationException.class, () -> filmService.addFilm(film));
        assertEquals(0, filmStorage.getFilmsQuantity());
    }

    @Test
    public void test_T0020_NS02_addValidation_negativeDuration() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2000, 12, 28));
        film.setDuration(Duration.ofMinutes(-90));
        film.setMpa(new Mpa(1, null));
        film.setGenres(new ArrayList<>());
        film.setDirectors(new ArrayList<>());

        assertEquals(0, filmStorage.getFilmsQuantity());
        assertThrows(ValidationException.class, () -> filmService.addFilm(film));
        assertEquals(0, filmStorage.getFilmsQuantity());
    }

    @Test
    public void test_T0010_PS01_updateValidation() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));
        film.setMpa(new Mpa(1, null));
        film.setGenres(new ArrayList<>());
        film.setDirectors(new ArrayList<>());

        film = filmService.addFilm(film);
        assertNotNull(film.getId());

        boolean actual = filmService.updateValidation(film);
        assertTrue(actual);
    }

    @Test
    public void test_T0010_NS01_updateValidation_noId() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));
        film.setMpa(new Mpa(1, null));
        film.setGenres(new ArrayList<>());
        film.setDirectors(new ArrayList<>());

        assertNull(film.getId());

        assertEquals(0, filmStorage.getFilmsQuantity());
        assertThrows(FilmNullValueValidationException.class, () -> filmService.updateFilm(film));
        assertEquals(0, filmStorage.getFilmsQuantity());

    }

    @Test
    public void test_T0010_NS02_updateValidation_incorrectId() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));
        film.setMpa(new Mpa(1, null));
        film.setGenres(new ArrayList<>());
        film.setDirectors(new ArrayList<>());

        film.setId(9999);

        assertEquals(0, filmStorage.getFilmsQuantity());
        assertThrows(FilmNotFoundException.class, () -> filmService.updateFilm(film));
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
        film.setMpa(new Mpa(1, null));
        film.setGenres(new ArrayList<>());
        film.setDirectors(new ArrayList<>());

        User user = new User();

        user.setLogin("dolore");
        user.setName("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        user.setFriends(new HashSet<>());

        Integer filmId = filmService.addFilm(film).getId();
        Integer userId = userService.createUser(user).getId();

        filmService.like(filmId, userId);

        assertEquals(1, filmStorage.getFilmsQuantity());
        assertEquals(1, filmStorage.getFilmById(filmId).getMpa().getId());
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

        Integer userId = userService.createUser(user).getId();

        assertThrows(FilmNotFoundException.class, () -> filmService.likeValidation(9999, userId));
    }

    @Test
    public void test_T0030_NS02_likeValidation_noContainsUserId() {
        assertEquals(0, filmStorage.getFilmsQuantity());

        Film film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));
        film.setMpa(new Mpa(1, null));
        film.setGenres(new ArrayList<>());
        film.setDirectors(new ArrayList<>());

        Integer filmId = filmService.addFilm(film).getId();

        assertThrows(UserNotFoundException.class, () -> filmService.likeValidation(filmId, 999));
    }
}