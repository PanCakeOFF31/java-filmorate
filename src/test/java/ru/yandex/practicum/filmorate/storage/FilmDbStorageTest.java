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
import ru.yandex.practicum.filmorate.storage.films.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.films.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genres.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genres.GenresStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikeStorage;
import ru.yandex.practicum.filmorate.storage.ratings.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.ratings.MpaStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@JdbcTest
@Sql(scripts = "file:./src/main/resources/schema.sql", executionPhase = BEFORE_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private FilmStorage filmStorage;

    @BeforeEach
    public void beforeEach() {
        LikeStorage likeStorage = new LikeDbStorage(jdbcTemplate);
        GenresStorage genresStorage = new GenreDbStorage(jdbcTemplate);
        MpaStorage mpaStorage = new MpaDbStorage(jdbcTemplate);
        filmStorage = new FilmDbStorage(jdbcTemplate, likeStorage, genresStorage, mpaStorage);
    }

    @Test
    void test_T0010_PS01_getFilmById() {
        Film film = new Film(1,
                "Болотная чешуя",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(150),
                0,
                new Mpa(1, "G"),
                new ArrayList<>());

        Integer addedFilmId = filmStorage.addFilm(film);

        assertNotNull(addedFilmId);
        assertTrue(addedFilmId > 0);

        Film savedFilm = filmStorage.getFilmById(1);

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
                0,
                new Mpa(1, "G"),
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
                0,
                new Mpa(1, "G"),
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
                0,
                new Mpa(1, "G"),
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
                0,
                new Mpa(999, "G"),
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
                0,
                new Mpa(1, "G"),
                new ArrayList<>());

        filmStorage.addFilm(film);
        assertEquals(1, filmStorage.getFilmsQuantity());

        Film changedFilm = new Film(1,
                "Болотная чепуха",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(150),
                0,
                new Mpa(2, "PG"),
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
                0,
                new Mpa(1, "G"),
                new ArrayList<>());

        filmStorage.addFilm(film);
        assertEquals(1, filmStorage.getFilmsQuantity());

        Film changedFilm = new Film(1,
                "Болотная чешуя",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(-150),
                0,
                new Mpa(1, "G"),
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
                0,
                new Mpa(1, "G"),
                new ArrayList<>());

        assertThrows(org.springframework.dao.EmptyResultDataAccessException.class, () -> filmStorage.updateFilm(film));

    }
}