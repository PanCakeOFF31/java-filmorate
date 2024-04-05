package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.director.DirectorDbStorage;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenresStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
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
        film.setId(addedFilmId);

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
        film.setId(addedId);

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

    @Test
    public void getSelectedFilms_ResultOk() {
        Film film1 = new Film(1,
                "Болотная чешуя",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(150),
                new Mpa(1, "G"),
                new ArrayList<>(),
                new ArrayList<>());

        Integer addedId1 = filmStorage.addFilm(film1);
        film1.setId(addedId1);

        Film film2 = new Film(2,
                "Болотная чешуя 2",
                "Описание фильма про водоем 2",
                LocalDate.of(1992, 2, 2),
                Duration.ofMinutes(120),
                new Mpa(1, "G"),
                new ArrayList<>(),
                new ArrayList<>());

        Integer addedId2 = filmStorage.addFilm(film2);
        film2.setId(addedId2);

        Film film3 = new Film(3,
                "Болотная чешуя 2",
                "Описание фильма про водоем 2",
                LocalDate.of(1993, 3, 3),
                Duration.ofMinutes(130),
                new Mpa(1, "G"),
                new ArrayList<>(),
                new ArrayList<>());

        Integer addedId3 = filmStorage.addFilm(film3);
        film3.setId(addedId3);

        List<Film> films = new ArrayList<>();
        films.add(film1);
        films.add(film3);

        List<Integer> filmsIds = new ArrayList<>();
        filmsIds.add(addedId1);
        filmsIds.add(addedId3);

        List<Film> savedFilms = filmStorage.getSelectedFilms(filmsIds);

        assertThat(savedFilms)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(films);
    }

    @Test
    public void test_getCommonFilms() {
        FriendshipStorage friendshipStorage = new FriendshipDbStorage(jdbcTemplate);
        UserStorage userStorage = new UserDbStorage(jdbcTemplate, friendshipStorage);
        LikeStorage likeStorage = new LikeDbStorage(jdbcTemplate);

        Film film1 = new Film(1,
                "Болотная чешуя",
                "Описание фильма про водоем",
                LocalDate.of(1990, 1, 1),
                Duration.ofMinutes(150),
                new Mpa(1, "G"),
                new ArrayList<>(),
                new ArrayList<>());
        film1.setId(filmStorage.addFilm(film1));

        Film film2 = new Film(2,
                "Наименование второго фильма",
                "Описание второго фильма",
                LocalDate.of(1992, 2, 2),
                Duration.ofMinutes(200),
                new Mpa(1, "G"),
                new ArrayList<>(),
                new ArrayList<>());
        film2.setId(filmStorage.addFilm(film2));

        Film film3 = new Film(3,
                "Наименование третьего фильма",
                "Описание третьего фильма",
                LocalDate.of(1993, 3, 3),
                Duration.ofMinutes(250),
                new Mpa(1, "G"),
                new ArrayList<>(),
                new ArrayList<>());
        film3.setId(filmStorage.addFilm(film3));

        User user1 = new User(1,
                "userOne@email.ru",
                "vanya123",
                LocalDate.of(1991, 1, 1),
                "Ivan Petrov",
                new HashSet<>());
        user1.setId(userStorage.addUser(user1));

        User user2 = new User(1,
                "userTwo@email.ru",
                "vanya321",
                LocalDate.of(1992, 2, 2),
                "Petr Ivanov",
                new HashSet<>());
        user2.setId(userStorage.addUser(user2));

        likeStorage.like(film1.getId(), user1.getId());
        likeStorage.like(film2.getId(), user1.getId());
        likeStorage.like(film2.getId(), user2.getId());
        likeStorage.like(film3.getId(), user2.getId());

        List<Film> expectedFilms = new ArrayList<>();
        expectedFilms.add(film2);

        List<Film> commonFilms = filmStorage.getCommonFilms(user1.getId(), user2.getId());
        assertEquals(expectedFilms, commonFilms);
    }
}