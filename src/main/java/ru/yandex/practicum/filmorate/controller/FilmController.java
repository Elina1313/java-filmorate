package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired(required = false)
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

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable int id) {
        log.info("Получен запрос GET к эндпоинту: /films/{}", id);
        return filmService.getFilm(id);
    }

    @GetMapping({"/popular?count={count}", "/popular"})
    public Collection<Film> findMostPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Получен запрос GET к эндпоинту: /films/popular?count={}", count);
        return filmService.getMostPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос PUT к эндпоинту: /films/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
        log.info("Обновлен объект {} с идентификатором {}, добавлен лайк от пользователя {}",
                Film.class.getSimpleName(), id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос DELETE к эндпоинту: films/{}/like/{}", id, userId);
        filmService.deleteLike(id, userId);
        log.info("Обновлен объект {} с идентификатором {}, удален лайк от пользователя {}",
                Film.class.getSimpleName(), id, userId);

    }
}