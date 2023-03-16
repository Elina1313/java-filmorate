package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> findAllUsers() {
        Collection<User> allUsers = users.values();
        if (users.isEmpty()) {
            allUsers.addAll(users.values());
        }
        return allUsers;
    }

    @Override
    public User createUser(User user) {
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistException(String.format(
                    "Пользователь с id:%d уже зарегистрирован.", user.getId()));
        }
        users.put(user.getId(), user);
        log.info("Добавлен пользователь, id = \"{}\"", user.getId());
        return user;
    }

    @Override
    public User getUser(final Integer id) {
        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        if (!findAllUsers().contains(user)) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    user.getId() + " не зарегистрирован!");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user.getId());
    }

    @Override
    public boolean addFriend(int userId, int friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        updateUser(user);
        updateUser(friend);
        return true;
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        User user = users.get(userId);
        user.getFriends().remove(friendId);
    }
}
