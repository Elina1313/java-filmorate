package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

@SpringBootTest
public class UserControllerTest {
    private UserController userController;
    private UserService userService;
    private UserStorage userStorage;
    private User user;

    public UserControllerTest(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @BeforeEach
    void beforeEach() {
        this.userService = new UserService(this.userStorage);
        this.userController = new UserController(this.userService);
        this.user = User.builder()
                .id(1)
                .email("mail@yandex.ru")
                .login("Login")
                .name("name")
                .birthday(LocalDate.of(1995, 12, 1))
                .build();
    }

    @AfterEach
    void afterEach() {
        this.userController = null;
        this.user = null;
        this.userService = null;
    }

    @Test
    public void wrongEmailOfUser() {
        user.setEmail("");
        Assertions.assertThrows(ValidationException.class, () -> this.userController.createUser(this.user));
        this.user.setEmail("yandex.ru");
        Assertions.assertThrows(ValidationException.class, () -> this.userController.createUser(this.user));
    }

    @Test
    public void wrongLoginOfUser() {
        this.user.setLogin("");
        Assertions.assertThrows(ValidationException.class, () -> this.userController.createUser(this.user));
        user.setLogin(" log in");
        Assertions.assertThrows(ValidationException.class, () -> this.userController.createUser(this.user));
    }

    @Test
    public void wrongDateOfUserBirthday() {
        this.user.setBirthday(LocalDate.of(2025, 12, 1));
        Assertions.assertThrows(ValidationException.class, () -> this.userController.createUser(this.user));
    }

    @Test
    public void wrongUserName() {
        this.user.setName("");
        this.userController.createUser(this.user);
        Assertions.assertEquals(this.user.getLogin(), this.user.getName());
    }

}