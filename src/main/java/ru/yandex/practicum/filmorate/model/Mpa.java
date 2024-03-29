package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class Mpa {
    private final int id;
    @JsonIgnore
    @JsonProperty
    @Size(max = 10)
    private final String name;
}
