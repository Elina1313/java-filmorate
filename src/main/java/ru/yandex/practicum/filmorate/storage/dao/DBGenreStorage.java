package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class DBGenreStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public DBGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void deleteFilmGenres(int filmId) {
        String deleteOldGenres = "DELETE FROM GenresGroup WHERE FILMID = ?";
        jdbcTemplate.update(deleteOldGenres, filmId);
    }

    @Override
    public void addFilmGenres(int filmId, List<Genre> genres) {
        Set<Genre> setGenre = new LinkedHashSet<>(genres);
        for (Genre genre : setGenre) {
            String setNewGenres = "INSERT INTO GenresGroup (FILMID, GENREID) VALUES (?, ?)";
            jdbcTemplate.update(setNewGenres, filmId, genre.getId());
        }
    }

    @Override
    public List<Genre> getGenresByFilmId(int filmId) {
        String sqlGenre = "SELECT GENRES.GENREID, NAME FROM GENRES " +
                "INNER JOIN GenresGroup GL ON GENRES.GENREID = GL.GENREID " +
                "WHERE FILMID = ?";
        return jdbcTemplate.query(sqlGenre, this::makeGenre, filmId);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlGenre = "SELECT GENREID, NAME FROM GENRES ORDER BY GENREID";
        return jdbcTemplate.query(sqlGenre, this::makeGenre);
    }

    @Override
    public Genre getGenreById(int genreId) {
        String sqlGenre = "SELECT * FROM GENRES WHERE GENREID = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlGenre, this::makeGenre, genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с идентификатором " +
                    genreId + " не зарегистрирован!");
        }
        return genre;
    }

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("GenreID"), resultSet.getString("Name"));
    }
}