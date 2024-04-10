package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
    private final EventStorage eventStorage;
    private final ExistChecker existChecker;

    public Event addEvent(final int userId,
                          final int entityId,
                          final EventType evenType,
                          final Operation operation) {
        log.debug("EventService - service.addEvent()");

        Event event = Event.builder()
                .timestamp(Instant.now().toEpochMilli())
                .eventType(evenType)
                .operation(operation)
                .userId(userId)
                .entityId(entityId)
                .build();

        int id = eventStorage.addEvent(event);
        log.info("Добавлено событие с id: {}", id);

        Event addedEvent = eventStorage.getEventById(id);
        log.info(addedEvent.toString());

        return addedEvent;
    }

    public List<Event> getUserFeed(final int userId) {
        log.debug("EventService - service.getUserFeed()");

        existChecker.userIsExist(userId);

        return eventStorage.getEventsByUserId(userId);
    }

}
