package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;

@SpringBootTest
public class FilmControllerTest {
    private FilmController filmController;
    private FilmService filmService;

    private UserService userService;
    private FilmStorage filmStorage;
    private Film film;

    /*@BeforeEach
    void beforeEach() {
        filmService = new FilmService(filmStorage, userService);
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
