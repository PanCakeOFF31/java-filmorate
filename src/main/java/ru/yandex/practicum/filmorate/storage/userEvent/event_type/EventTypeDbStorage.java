package ru.yandex.practicum.filmorate.storage.userEvent.event_type;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventTypeDbStorage implements EventTypeStorage {
    private final JdbcTemplate jdbcTemplate;
}