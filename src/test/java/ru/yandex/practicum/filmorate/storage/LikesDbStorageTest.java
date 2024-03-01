package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.storage.likes.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikeStorage;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@JdbcTest
@Sql(scripts = "file:./src/main/resources/schema.sql", executionPhase = BEFORE_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikesDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private LikeStorage likeStorage;

    @BeforeEach
    public void beforeEach() {
        likeStorage = new LikeDbStorage(jdbcTemplate);
    }
    @Test
    void name() {
    }
}
