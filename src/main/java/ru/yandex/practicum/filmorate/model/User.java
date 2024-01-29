package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    @NotNull
    @Email
    private String email;
    @NotNull
    @NotBlank
    @NotEmpty
    private String login;
    @NotNull
    private String name;
    @NotNull
    @Past
    private LocalDate birthday;

}
