package ru.yandex.practicum.filmorate.storage.userEvent.operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OperationDbStorage implements OperationStorage {
    private final JdbcTemplate jdbcTemplate;
}
