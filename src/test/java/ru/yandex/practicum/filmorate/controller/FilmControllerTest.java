package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    private void initialize() {
        filmController = new FilmController();
    }

    @Test
    public void T0010_PS01_updateValidation() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));

        filmController.addFilm(film);
        assertEquals(1, film.getId());

        film.setName("some");
        film.setDescription("some");
        film.setReleaseDate(LocalDate.of(2000, 12, 3));
        film.setDuration(Duration.ofMinutes(900));

        boolean actual = filmController.updateValidation(film);
        assertTrue(actual);
    }

    @Test
    public void T0010_NS01_updateValidation_noId() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));

        assertNull(film.getId());
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film));

        boolean actual = filmController.updateValidation(film);
        assertFalse(actual);
    }

    @Test
    public void T0010_NS02_updateValidation_incorrectId() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));

        film.setId(9999);
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film));

        boolean actual = filmController.updateValidation(film);
        assertFalse(actual);
    }

    @Test
    public void T0011_PS01_addValidation() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));

        filmController.addFilm(film);
        assertEquals(1, film.getId());

        boolean actual = filmController.updateValidation(film);
        assertTrue(actual);
    }

    @Test
    public void T0011_NS01_addValidation_releaseDate() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(Duration.ofMinutes(90));

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));

        boolean actual = filmController.addValidation(film);
        assertFalse(actual);
    }

    @Test
    public void T0011_NS02_addValidation_duration() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2000, 12, 28));
        film.setDuration(Duration.ofMinutes(-90));

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));

        boolean actual = filmController.addValidation(film);
        assertFalse(actual);
    }
}