package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Review {
    @JsonProperty("reviewId")
    private final int id;
    @NotBlank
    @Size(max = 1000)
    private final String content;
    @NotNull
    private final Boolean isPositive;
    @NotNull
    private final Integer userId;
    @NotNull
    private final Integer filmId;
    //    Значение рассчитывается на основании суммы лайков и дизлайков из таблицы review_like
    private final int useful;
}
