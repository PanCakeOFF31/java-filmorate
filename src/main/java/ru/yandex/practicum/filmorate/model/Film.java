package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
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
    @NotNull
    private FilmRating rating;
    @Null
    private Set<Integer> likes;

    public boolean like(int userId) {
        return likes.add(userId);
    }

    public boolean unlike(int userId) {
        return likes.remove(userId);
    }

    public int likeQuantity() {
        return likes.size();
    }
}