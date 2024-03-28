package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private int rate;
    @NotNull
    private Mpa mpa;
    private List<Genre> genres;

    public Film(Film otherFilm) {
        this.id = otherFilm.getId();
        this.name = otherFilm.getName();
        this.description = otherFilm.getDescription();
        this.releaseDate = otherFilm.getReleaseDate();
        this.duration = otherFilm.getDuration();
        this.rate = otherFilm.getRate();
        this.mpa = otherFilm.getMpa();
        this.genres = otherFilm.getGenres();
    }
}