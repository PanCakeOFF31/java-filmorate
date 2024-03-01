package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@JdbcTest
@Sql(scripts = "file:./src/main/resources/schema.sql", executionPhase = BEFORE_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikesDbStorageTest {
}
