package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@Slf4j
@Service
public class UserService {
    private static int generateId = 0;
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User createUser(final User user) {
        validation(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validation(user);
        return userStorage.updateUser(user);
    }

    public void addFriend(final int supposedUserId, final int supposedFriendId) {
        if ((supposedUserId <= 0) || (supposedFriendId <= 0)) {
            throw new NotFoundException("id is less than zero");
        }
        userStorage.addFriend(supposedUserId, supposedFriendId);
    }

    public void deleteFriend(final int supposedUserId, final int supposedFriendId) {
        userStorage.deleteFriend(supposedUserId, supposedFriendId);
    }

    public Collection<User> getFriends(final int supposedUserId) {
        User user = getUser(supposedUserId);
        Collection<User> friends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            friends.add(userStorage.getUser(id));
        }
        return friends;
    }

    public Collection<User> getCommonFriends(final int supposedUserId, final int supposedOtherId) {
        User user = getStoredUser(supposedUserId);
        User otherUser = getStoredUser(supposedOtherId);
        Collection<User> commonFriends = new HashSet<>();
        if (((user.getFriends() == null) || (user.getFriends().isEmpty())) || ((otherUser.getFriends() == null) || (otherUser.getFriends().isEmpty()))) {
            return Collections.emptyList();
        }
        for (Integer id : user.getFriends()) {
            if (otherUser.getFriends().contains(id)) {
                commonFriends.add(userStorage.getUser(id));
            }
        }
        return commonFriends;
    }


    private static void validation(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) { //
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDateTime.now().toLocalDate().plusDays(1)))
            throw new ValidationException("Дата рождения не может быть в будущем");
        if (user.getName() == null) {
            log.info("Имя не задано, в таком случае будет использован логин {}",
                    user.getLogin());
            user.setName(user.getLogin());
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Задано пустое имя, в таком случае будет использован логин {}",
                    user.getLogin());
        }
        if (user.getId() == 0) {
            user.setId(++generateId);
        }
    }

    public User getUser(final int id) {
        return getStoredUser(id);
    }

    private User getStoredUser(final int supposedId) {
        if (supposedId == Integer.MIN_VALUE) {
            throw new NotFoundException("Не удалось распознать идентификатор пользователя: " +
                    "значение " + supposedId);
        }
        User user = userStorage.getUser(supposedId);
        if (user == null) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    supposedId + " не зарегистрирован!");
        }
        return user;
    }

}
