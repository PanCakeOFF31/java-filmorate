package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    @NotNull
    @Email
    @Size(max = 128)
    private String email;
    @NotBlank
    @Size(max = 64)
    private String login;
    @NotNull
    @Past
    private LocalDate birthday;
    @Size(max = 64)
    private String name;
    private Set<Integer> friends;

    public User(User otherUser) {
        this.id = otherUser.id;
        this.email = otherUser.email;
        this.login = otherUser.login;
        this.birthday = otherUser.birthday;
        this.name = otherUser.name;
        this.friends = otherUser.friends;
    }
}