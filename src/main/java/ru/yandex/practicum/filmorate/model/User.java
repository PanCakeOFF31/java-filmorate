package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public boolean unfriend(int friendId) {
        return friends.remove(friendId);
    }

    public int friendsQuantity() {
        return friends.size();
    }
}