package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    @NotNull
    @Email
    private String email;
    @NotBlank
    private String login;
    @NotNull
    @Past
    private LocalDate birthday;
    //    Последний тест от Яндекс "POST Create user with empty name" почему-то не содержит поле name
//    Поэтому убрал ограничение javax.validation.constraints
    private String name = "";
}
