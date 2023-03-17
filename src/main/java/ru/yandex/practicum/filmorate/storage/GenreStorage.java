package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreStorage {

    List<Genre> getAllGenres();

    List<Genre> getGenresByFilmId(int filmId);

    Genre getGenreById(int genreId);

    void addFilmGenres(int filmId, List<Genre> genres);

    void deleteFilmGenres(int filmId);
}
