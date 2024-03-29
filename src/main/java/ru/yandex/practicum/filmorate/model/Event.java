package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
public class Event {
    private final int eventId; // PK
    @NotNull
    private final long timestamp;
    @NotNull
    private final int userId;
    private final EvenType eventType;
    private final Operation operation;
    @NotNull
    private final int entityId;
}
