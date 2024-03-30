package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class Director {
    private final int id;
    @JsonIgnore
    @JsonProperty
    @Size(max = 128)
    private final String name;
}