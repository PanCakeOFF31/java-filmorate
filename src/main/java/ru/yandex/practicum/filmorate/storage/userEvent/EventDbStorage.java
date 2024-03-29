package ru.yandex.practicum.filmorate.storage.userEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventDbStorage implements EventStorage{
    private final JdbcTemplate jdbcTemplate;
}
