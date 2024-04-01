package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class Event {
    private final int eventId; // PK
    @NotNull
    private final Long timestamp;
    @NotNull
    private final Integer userId;
    @NotNull
    private final EventType eventType;
    @NotNull
    private final Operation operation;
    @NotNull
    private final Integer entityId; // Идентификатор сущности
}
