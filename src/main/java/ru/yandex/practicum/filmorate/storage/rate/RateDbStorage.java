package ru.yandex.practicum.filmorate.storage.rate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

// Будет использован для последнего задания после review
@Component
@RequiredArgsConstructor
@Slf4j
public class RateDbStorage implements RateStorage {
    private final JdbcTemplate jdbcTemplate;
}
