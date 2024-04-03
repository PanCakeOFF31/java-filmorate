package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class Director {
    private final int id;
    @NotBlank
    @Size(max = 128)
    private final String name;
}
