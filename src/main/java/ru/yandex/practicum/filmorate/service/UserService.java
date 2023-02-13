package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class UserService {

    private static int generateId = 0;
    private Map<Integer, User> users = new HashMap<>();

    public Collection<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User createUser(User user) {
        validation(user);
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistException(String.format(
                    "Пользователь с id:%d уже зарегистрирован.", user.getId()));
        }
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        validation(user);
        if (!users.containsKey(user.getId())) { //
            throw new NotFoundException("Пользователь с идентификатором " +
                    user.getId() + " не зарегистрирован!");
        }
        users.put(user.getId(), user);
        return user; //
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
}
