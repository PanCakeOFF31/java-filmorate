package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private FilmService service;
    private FilmStorage storage;
    private Film film;

    @BeforeEach
    public void initialize() {
        storage = new InMemoryFilmStorage();
        service = new FilmService(storage);
        filmController = new FilmController(service);
    }


    @Test
    public void test_T0011_PS01_addValidation() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));

        boolean actual = service.addValidation(film);
        assertTrue(actual);

        assertEquals(0, storage.getFilmsQuantity());

        service.addFilm(film);
        assertNotNull(film.getId());

        assertEquals(1, storage.getFilmsQuantity());
    }

    @Test
    public void test_T0011_NS01_addValidation_releaseDate() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(Duration.ofMinutes(90));

        boolean actual = service.addValidation(film);
        assertFalse(actual);

        assertEquals(0, storage.getFilmsQuantity());
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals(0, storage.getFilmsQuantity());
    }

    @Test
    public void test_T0011_NS02_addValidation_negativeDuration() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2000, 12, 28));
        film.setDuration(Duration.ofMinutes(-90));

        boolean actual = service.addValidation(film);
        assertFalse(actual);

        assertEquals(0, storage.getFilmsQuantity());
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals(0, storage.getFilmsQuantity());
    }

    @Test
    public void test_T0010_PS01_updateValidation() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));

        filmController.addFilm(film);
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

        boolean actual = service.updateValidation(film);
        assertFalse(actual);

        assertEquals(0, storage.getFilmsQuantity());
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
        assertEquals(0, storage.getFilmsQuantity());

    }

    @Test
    public void test_T0010_NS02_updateValidation_incorrectId() {
        film = new Film();

        film.setName("some name");
        film.setDescription("some description");
        film.setReleaseDate(LocalDate.of(2020, 12, 3));
        film.setDuration(Duration.ofMinutes(90));

        film.setId(9999);

        boolean actual = service.updateValidation(film);
        assertFalse(actual);

        assertEquals(0, storage.getFilmsQuantity());
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
        assertEquals(0, storage.getFilmsQuantity());

    }
}
