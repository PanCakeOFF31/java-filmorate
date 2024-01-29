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
    private String login;
    @NotNull
    @Past
    private LocalDate birthday;
    private String name;

//    Последний тест от Яндекс "POST Create user with empty name" почему-то не содержит поле name
//    Использую блок инициализации объекта для явности
    {
        name = "";
    }
}
