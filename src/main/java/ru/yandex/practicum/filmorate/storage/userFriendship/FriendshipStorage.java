package ru.yandex.practicum.filmorate.storage.userFriendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {
    boolean addToFriend(int userId, int friendId);

    boolean deleteFromFriend(int userId, int friendId);

    int getUserFriendCounts(int userId);

    List<Integer> getUserFriendsAsId(int userId);

    List<User> getUserFriendsAsUsers(int userId);

    List<User> getCommonFriendsAsUsers(int userId, int otherUserId);
}
