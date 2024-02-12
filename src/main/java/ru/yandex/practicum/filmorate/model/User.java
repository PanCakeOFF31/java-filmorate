package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    @Null
    private Set<Integer> friends;
}
