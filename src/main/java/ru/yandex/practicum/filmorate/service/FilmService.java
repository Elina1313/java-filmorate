package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Validator;
import java.time.LocalDate;
import java.util.*;

@Service
public class FilmService {
    private int generateId = 0;


    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getAllFilms() {

        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        validation(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        validation(film);
        return filmStorage.updateFilm(film);
    }

    public boolean addLike(final int id, int userId) {
        Film film = getStoredFilm(id);
        User user = userStorage.getUser(userId);
        return filmStorage.addLike(film.getId(), user.getId());
    }

    private Film getStoredFilm(int id) {
        Film film = filmStorage.getFilm(id);
        if (film == null) {
            throw new NotFoundException("Фильм с идентификатором " +
                    id + " не зарегистрирован!");
        }
        return film;
    }

    public void deleteLike(final int id, final int userId) {
        Film film = getStoredFilm(id);
        User user = userStorage.getUser(userId);
        filmStorage.deleteLike(film.getId(), user.getId());
    }

    public Collection<Film> getMostPopularFilms(Integer size) {
        if (size == 0) {
            size = 10;
        }
        return filmStorage.getMostPopularFilms(size);
    }

    public Film getFilm(int id) {
        return getStoredFilm(id);
    }

    private void validation(Film film) {
        if (film.getName().isEmpty()) {
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
