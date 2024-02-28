package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface FriendshipStorage {
    void addToFriend(int userId, int friendId);
    void deleteFromFriend(int userId, int friendId);
    Set<Integer> getUserFriendsId(int userId);
    List<User> getUserFriends(int userId);
    List<User> getCommonFriends(int userId, int otherUserId);
}
