package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание пользователя");
        User addUser = userService.createUser(user);
        log.info("Фильм с id: {} добавлен", addUser.getId());
        return addUser;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Получен запрос на обновление пользователя");
        User updateUser = userService.updateUser(user);
        log.info("Фильм с id: {} добавлен", updateUser.getId());
        return updateUser;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        log.info("Получен запрос на получение списка всех пользователей");
        return userService.findAllUsers();
    }
}
