package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @NotNull
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Duration duration;
    @Null
    private Set<Integer> likes;
}
