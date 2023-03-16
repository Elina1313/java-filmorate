package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAllUsers();

    User createUser(User user);

    User getUser(final Integer id);

    User updateUser(User user);

    void deleteUser(User user);

    boolean addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

}
