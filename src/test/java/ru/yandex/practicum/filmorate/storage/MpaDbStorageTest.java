package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.filmMpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.filmMpa.MpaStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private MpaStorage mpaStorage;

    @BeforeEach
    public void beforeEach() {
        mpaStorage = new MpaDbStorage(jdbcTemplate);
    }

    @Test
    public void test_T0010_PS01_getMpaById() {
        Mpa mpa = new Mpa(1, "G");

        Mpa gotMpa = mpaStorage.getMpa(1);
        assertThat(gotMpa)
                .isNotNull()
                .isEqualTo(mpa);
    }

    @Test
    public void test_T0010_NS01_getMpaById_unknownMpa() {
        assertThrows(org.springframework.dao.EmptyResultDataAccessException.class, () -> mpaStorage.getMpa(9999));
    }

    @Test
    public void test_T0020_PS01_getMpas() {
        List<Mpa> mpas = mpaStorage.getAllMpa();
        assertEquals(5, mpas.size());

        Mpa mpa = new Mpa(2, "PG");
        assertEquals(mpa, mpas.get(1));
    }
}