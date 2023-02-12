package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class FilmService {
    private int generateId = 0;
    private Map<Integer, Film> films = new HashMap<>();

    public Collection<Film> getAllFilms() {
        return films.values();
    }

    public Film addFilm(Film film) {
        validation(film);
        if (films.containsKey(film.getId())) {
            throw new UserAlreadyExistException(String.format(
                    "Фильм с id:%d уже зарегистрирован.", film.getId()));
        }
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        validation(film);
        if (!films.containsKey(film.getId())) { //
            throw new NotFoundException("Фильм с идентификатором " +
                    film.getId() + " не зарегистрирован!");
        }
        films.put(film.getId(), film);
        return film;
    }

    public void validation(Film film) {
        if (film.getName().isEmpty()) { //
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        generatorId(film);
    }

    private void generatorId(Film film) {
        if (film.getId() == 0) {
            film.setId(++generateId);
        }
    }

}
