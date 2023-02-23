package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    public UserController(@Autowired(required = false) UserService userService) {
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

    @GetMapping("/{id}/friends")
    public Collection<User> findFriends(@PathVariable int id) {
        log.info("Получен запрос GET к эндпоинту: /users/{}/friends", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable int id) {
        log.info("Получен запрос GET к эндпоинту: /users/{}/", id);
        return userService.getUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получен запрос GET к эндпоинту: /users/{}/friends/common/{}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен запрос PUT к эндпоинту: /users/{}/friends/{}", id, friendId);
        //userService.updateUser(user).getId();
        userService.addFriend(id, friendId);
        log.info("Обновлен объект {} с идентификатором {}. Добавлен друг {}",
                User.class.getSimpleName(), id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен запрос DELETE к эндпоинту: /users/{}/friends/{}", id, friendId);
        userService.deleteFriend(id, friendId);
        log.info("Обновлен объект {} с идентификатором {}. Удален друг {}",
                User.class.getSimpleName(), id, friendId);
    }
}
