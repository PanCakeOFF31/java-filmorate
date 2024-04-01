package ru.yandex.practicum.filmorate.storage.userEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public int addEvent(final Event event) {
        log.debug("EventDbStorage - addEvent()");

        log.info("Начала добавления события в таблицу: {}", event.toString());

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("event")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();

        params.put("time_stamp", event.getTimestamp());
        params.put("user_id", event.getUserId());
        params.put("entity_id", event.getEntityId());
        params.put("event_type", event.getEventType().toString());
        params.put("operation", event.getOperation().toString());

        int eventId = jdbcInsert.executeAndReturnKey(params).intValue();
        log.info("Занесено событие с id: {}", eventId);

        return eventId;
    }

    @Override
    public List<Event> getEventsByUserId(final int userId) {
        log.debug("EventDbStorage - getEventsByUserId()");

        String sqlRequest = "SELECT * FROM event WHERE user_id = ?;";
        RowMapper<Event> eventMapper = (rs, rowNum) -> makeEvent(rs);

        return jdbcTemplate.query(sqlRequest, eventMapper, userId);

    }

    @Override
    public Event getEventById(int eventId) {
        log.debug("EventDbStorage - getEventById())");

        String sqlRequest = "SELECT * FROM event WHERE id = ?";
        RowMapper<Event> eventMapper = (rs, rowNum) -> makeEvent(rs);

        return jdbcTemplate.queryForObject(sqlRequest, eventMapper, eventId);
    }

    public Event makeEvent(ResultSet rs) throws SQLException {
        log.debug("EventDbStorage - makeEvent())");

        int eventId = rs.getInt("id");
        long timestamp = rs.getLong("time_stamp");
        int userId = rs.getInt("user_id");
        EventType eventType = EventType.valueOf(rs.getString("event_type"));
        Operation operation = Operation.valueOf(rs.getString("operation"));
        int entityId = rs.getInt("entity_id");

        return Event.builder()
                .eventId(eventId)
                .timestamp(timestamp)
                .userId(userId)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();
    }
}
