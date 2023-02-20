package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на добавление фильма");
        Film addFilm = filmService.addFilm(film);
        log.info("Фильм с id: {} добавлен", addFilm.getId());
        return addFilm;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Получен запрос на обновление фильма");
        Film updateFilm = filmService.updateFilm(film);
        log.info("Фильм с id: {} обновлен", updateFilm.getId());
        return updateFilm;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос на получение списка фильмов");
        return filmService.getAllFilms();
    }
}