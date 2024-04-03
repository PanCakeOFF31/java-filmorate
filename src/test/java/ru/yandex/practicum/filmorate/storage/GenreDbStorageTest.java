package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenresStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private GenresStorage genresStorage;

    @BeforeEach
    public void beforeEach() {
        genresStorage = new GenreDbStorage(jdbcTemplate);
    }

    @Test
    public void test_T0010_PS01_getGenreById() {
        Genre mpa = new Genre(4, "Триллер");
        Genre gotMpa = genresStorage.getGenre(4);

        assertThat(gotMpa)
                .isNotNull()
                .isEqualTo(mpa);
    }

    @Test
    public void test_T0010_NS01_getGenreById_unknownUser() {
        assertThrows(org.springframework.dao.EmptyResultDataAccessException.class, () -> genresStorage.getGenre(9999));
    }

    @Test
    public void test_T0020_PS01_getGenres() {
        List<Genre> mpas = genresStorage.getAllGenres();
        assertEquals(6, mpas.size());

        Genre mpa = new Genre(2, "Драма");
        assertEquals(mpa, mpas.get(1));
    }
}