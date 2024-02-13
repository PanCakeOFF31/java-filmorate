package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
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

    public User(User otherUser) {
        this.id = otherUser.id;
        this.email = otherUser.email;
        this.login = otherUser.login;
        this.birthday = otherUser.birthday;
        this.name = otherUser.name;
        this.friends = otherUser.friends;
    }

    public boolean toFriend(int friendId) {
        return friends.add(friendId);
    }

    public boolean unfriend(int friendId) {
        return friends.remove(friendId);
    }

    public int friendsQuantity() {
        return friends.size();
    }
}
