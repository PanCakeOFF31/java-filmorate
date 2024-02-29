package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface FriendshipStorage {
    void addToFriend(int userId, int friendId);
    void deleteFromFriend(int userId, int friendId);
    List<Integer> getUserFriendsAsId(int userId);
    List<User> getUserFriendsAsUsers(int userId);
    List<User> getCommonFriendsAsUsers(int userId, int otherUserId);
}
