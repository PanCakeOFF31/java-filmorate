package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmDirector.DirectorDbStorage;
import ru.yandex.practicum.filmorate.storage.filmDirector.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.filmGenre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.filmGenre.GenresStorage;
import ru.yandex.practicum.filmorate.storage.filmMpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.filmMpa.MpaStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
//

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private FilmStorage filmStorage;

    @BeforeEach
    public void beforeEach() {
        GenresStorage genresStorage = new GenreDbStorage(jdbcTemplate);
        MpaStorage mpaStorage = new MpaDbStorage(jdbcTemplate);
        DirectorStorage directorStorage = new DirectorDbStorage(jdbcTemplate);
        filmStorage = new FilmDbStorage(jdbcTemplate, genresStorage, mpaStorage, directorStorage);
    }

    @Test
    void test_T0010_PS01_getFilmById() {
        Film film = new Film(1,
                "Болотная чешуя",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(150),
                new Mpa(1, "G"),
                new ArrayList<>(),
                new ArrayList<>());

        Integer addedFilmId = filmStorage.addFilm(film);

        assertNotNull(addedFilmId);
        assertTrue(addedFilmId > 0);

        Film savedFilm = filmStorage.getFilmById(addedFilmId);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void test_T0010_NS01_getFilmById_unknownFilm() {
        assertThrows(org.springframework.dao.EmptyResultDataAccessException.class, () -> filmStorage.getFilmById(9999));
    }

    @Test
    public void test_T0020_PS01_getFilmsQuantity() {
        Film film1 = new Film(1,
                "Болотная чешуя",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(150),
                new Mpa(1, "G"),
                new ArrayList<>(),
                new ArrayList<>());

        Film film2 = new Film(film1);
        film2.setName("Не болотная чешуя");

        Integer addedFilm1Id = filmStorage.addFilm(film1);
        Integer addedFilm2Id = filmStorage.addFilm(film2);

        assertNotNull(addedFilm1Id);
        assertTrue(addedFilm1Id > 0);
        assertNotNull(addedFilm2Id);
        assertTrue(addedFilm2Id > 0);

        assertEquals(2, filmStorage.getFilmsQuantity());
    }


    @Test
    public void test_T0030_PS01_getFilms() {
        assertEquals(0, filmStorage.getFilmsQuantity());

        Film film1 = new Film(1,
                "Болотная чешуя",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(150),
                new Mpa(1, "G"),
                new ArrayList<>(),
                new ArrayList<>());

        Film film2 = new Film(film1);
        film2.setName("Не болотная чешуя");

        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        assertEquals(2, filmStorage.getFilmsQuantity());

        List<Film> films = filmStorage.getFilms();
        assertEquals(2, films.size());

        assertEquals(film1.getName(), films.get(0).getName());
        assertEquals(film2.getName(), films.get(1).getName());
    }

    @Test
    public void test_T0040_PS01_addFilm() {
        assertEquals(0, filmStorage.getFilmsQuantity());

        Film film = new Film(1,
                "Болотная чешуя",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(150),
                new Mpa(1, "G"),
                new ArrayList<>(),
                new ArrayList<>());

        Integer addedId = filmStorage.addFilm(film);
        Film savedFilm = filmStorage.getFilmById(addedId);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);

        assertEquals(1, filmStorage.getFilmsQuantity());
    }

    @Test
    public void test_T0040_NS02_addFilm_noMpaId() {
        assertEquals(0, filmStorage.getFilmsQuantity());

        Film film = new Film(1,
                "Болотная чешуя",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(150),
                new Mpa(999, "G"),
                new ArrayList<>(),
                new ArrayList<>());

        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> filmStorage.addFilm(film));
    }

    @Test
    public void test_T0050_PS01_updateFilm() {
        assertEquals(0, filmStorage.getFilmsQuantity());

        Film film = new Film(1,
                "Болотная чешуя",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(150),
                new Mpa(1, "G"),
                new ArrayList<>(),
                new ArrayList<>());

        int id = filmStorage.addFilm(film);
        assertEquals(1, filmStorage.getFilmsQuantity());

        Film changedFilm = new Film(id,
                "Болотная чепуха",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(150),
                new Mpa(2, "PG"),
                new ArrayList<>(),
                new ArrayList<>());

        Film updatedFilm = filmStorage.updateFilm(changedFilm);

        assertEquals(1, filmStorage.getFilmsQuantity());

        assertThat(updatedFilm)
                .isNotNull()
                .isEqualTo(changedFilm);
    }

    @Test
    public void test_T0050_NS01_updateFilm_checkDurationConstraint() {
        assertEquals(0, filmStorage.getFilmsQuantity());

        Film film = new Film(1,
                "Болотная чешуя",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(150),
                new Mpa(1, "G"),
                new ArrayList<>(),
                new ArrayList<>());

        int id = filmStorage.addFilm(film);
        assertEquals(1, filmStorage.getFilmsQuantity());

        Film changedFilm = new Film(id,
                "Болотная чешуя",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(-150),
                new Mpa(1, "G"),
                new ArrayList<>(),
                new ArrayList<>());

        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> filmStorage.updateFilm(changedFilm));
    }

    @Test
    public void test_T0050_NS02_updateFilm_unknownFilm() {
        Film film = new Film(9999,
                "Болотная чешуя",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(150),
                new Mpa(1, "G"),
                new ArrayList<>(),
                new ArrayList<>());

        assertThrows(org.springframework.dao.EmptyResultDataAccessException.class, () -> filmStorage.updateFilm(film));

    }
}