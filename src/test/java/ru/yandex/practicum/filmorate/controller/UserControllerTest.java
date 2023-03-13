package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
    @MockBean
    private UserController userController;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnCode200GetUser() throws Exception {
        User user = User.builder()
                .id(1)
                .email("mail@yandex.ru")
                .login("Login")
                .name("name")
                .birthday(LocalDate.of(1995, 12, 1))
                .build();
        Mockito.when(userController.findAllUsers()).thenReturn(Collections.singletonList(user));
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(user))));
    }

    @Test
    public void shouldReturnCode200FindCommonFriends() throws Exception {
        User user = new User(2,
                "mail@yandex.ru",
                "Login",
                "name",
                LocalDate.of(1995, 12, 1),
                new HashSet<>());
        User user2 = new User(3,
                "mail@yandex.ru",
                "Login",
                "name",
                LocalDate.of(1995, 12, 1),
                new HashSet<>());
        User user3 = new User(4,
                "mail@yandex.ru",
                "Login",
                "name",
                LocalDate.of(1995, 12, 1),
                new HashSet<>());
        user.addFriend(3);
        user3.addFriend(3);
        userService.createUser(user);
        userService.createUser(user2);
        userService.createUser(user3);
        List<User> users = List.of(user2);
        Mockito.when(userController.findCommonFriends(Mockito.anyInt(), Mockito.anyInt())).thenReturn(users);
        mockMvc.perform(get("/users/2/friends/common/4"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)))
                .andReturn();
    }


    /*public UserControllerTest(UserStorage userStorage) {
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
    }*/

}