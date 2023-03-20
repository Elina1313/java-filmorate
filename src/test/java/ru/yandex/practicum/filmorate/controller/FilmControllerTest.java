package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private FilmController filmController;

    @Test
    void shouldReturn200whenGetFilms() throws Exception {
        Film film = new Film();
        film.setName("Correct Name");
        film.setDescription("Correct description");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100L);
        Mockito.when(filmController.findAll()).thenReturn(Collections.singletonList(film));
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(film))));
    }

    /*@Test
    void shouldReturn200whenPostCorrectFilmData() throws Exception {
        Film film = new Film();
        film.setName("Correct Name");
        film.setDescription("Correct description");
        film.setReleaseDate(LocalDate.of(1995,5,26));
        film.setDuration(100L);
        film.setRate(5);
        Mockito.when(filmController.addFilm(Mockito.any())).thenReturn(film);
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(film)));
    }*/



    /*private FilmService filmService;

    private UserService userService;
    private FilmStorage filmStorage;
    private Film film;

    @BeforeEach
    void beforeEach() {
        Validator validator = null;
        filmService = new FilmService(validator, filmStorage, userService);
        filmController = new FilmController(filmService);
        film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1995, 12, 1))
                .duration((long) 1.55)
                .build();
    }

    @AfterEach
    void afterEach() {
        filmController = null;
        film = null;
        filmService = null;
    }

    @Test
    public void emptyNameOfFilm() {
        film.setName("");
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void maxLengthOfFilmDescription() {
        String newString = "a".repeat(250);
        film.setDescription(newString);
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void wrongReleaseDate() {
        film.setReleaseDate(LocalDate.of(1885,12,1));
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void wrongDurationOfFilm() {
        film.setDuration((long) -1.55);
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }*/
}
