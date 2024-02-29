package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@RequiredArgsConstructor
@Data
public class Mpa {
    private final int id;
    @JsonIgnore
    @JsonProperty
    private final String name;
}
