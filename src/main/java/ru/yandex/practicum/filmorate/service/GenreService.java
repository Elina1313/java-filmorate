package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.List;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> getAllGenres() {
        return (List<Genre>) genreStorage.getAllGenres();
    }

    public List<Genre> getFilmGenres(int filmId) {
        return (List<Genre>) genreStorage.getGenresByFilmId(filmId);
    }

    public Genre getGenre(int genreId) {
        return genreStorage.getGenreById(genreId);
    }

    public void deleteFilmGenres(int filmId) {
        genreStorage.deleteFilmGenres(filmId);
    }

    public void addFilmGenres(int filmId, List<Genre> genres) {
        genreStorage.addFilmGenres(filmId, genres);
    }
}
