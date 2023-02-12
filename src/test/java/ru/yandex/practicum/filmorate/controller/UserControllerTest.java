package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

@SpringBootTest
public class UserControllerTest {
    private UserController userController;
    private UserService userService;
    private User user;

    @BeforeEach
    void beforeEach() {
        userService = new UserService();
        userController = new UserController(userService);
        user = User.builder()
                .id(1)
                .email("mail@yandex.ru")
                .login("Login")
                .name("name")
                .birthday(LocalDate.of(1995, 12, 1))
                .build();
    }

    @AfterEach
    void afterEach() {
        userController = null;
        user = null;
        userService = null;
    }

    @Test
    public void wrongEmailOfUser() {
        user.setEmail("");
        Assertions.assertThrows(ValidationException.class, () -> userController.createUser(user));
        user.setEmail("yandex.ru");
        Assertions.assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void wrongLoginOfUser() {
        user.setLogin("");
        Assertions.assertThrows(ValidationException.class, () -> userController.createUser(user));
        user.setLogin(" log in");
        Assertions.assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void wrongDateOfUserBirthday() {
        user.setBirthday(LocalDate.of(2025, 12, 1));
        Assertions.assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void wrongUserName() {
        user.setName("");
        userController.createUser(user);
        Assertions.assertEquals(user.getLogin(), user.getName());
    }

}
