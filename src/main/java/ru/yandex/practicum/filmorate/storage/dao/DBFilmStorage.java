package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.util.List;
import java.util.Objects;

@Component
public class DBFilmStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(DBFilmStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final GenreService genreService;

    public DBFilmStorage(JdbcTemplate jdbcTemplate, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreService = genreService;
    }

    @Override
    public Film getFilm(int filmId) {

        String sqlFilm = "SELECT * FROM FILM " +
                "INNER JOIN RatingMPA R ON FILM.RatingID = R.RatingID " +
                "WHERE FilmID = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlFilm, (rs, rowNum) -> makeFilm(rs), filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с идентификатором " +
                    filmId + " не зарегистрирован!");
        }
        log.info("Найден фильм: {} {}", film.getId(), film.getName());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "select * from FILM " +
                "INNER JOIN RatingMPA R on Film.RatingID = R.RatingID ";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> makeFilm(resultSet));
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into FILM " +
                "(NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, RATINGID) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedStatement.setLong(4, film.getDuration());
            preparedStatement.setInt(5, film.getRate());
            preparedStatement.setInt(6, Math.toIntExact(film.getMpa().getId()));
            return preparedStatement;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        if (!film.getGenres().isEmpty()) {
            genreService.addFilmGenres(film.getId(), film.getGenres());
        }
        if (film.getLikes() != null) {
            for (Integer userId : film.getLikes()) {
                addLike(film.getId(), userId);
            }
        }
        return getFilm(id);
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update FILM " +
                "set NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, RATE = ? ,RATINGID = ? " +
                "where FILMID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());

        genreService.deleteFilmGenres(film.getId());
        if (!film.getGenres().isEmpty()) {
            genreService.addFilmGenres(film.getId(), film.getGenres());
        }

        if (film.getLikes() != null) {
            for (Integer userId : film.getLikes()) {
                addLike(film.getId(), userId);
            }
        }
        return getFilm(film.getId());
    }

    @Override
    public boolean addLike(int filmId, int userId) {
        String sql = "SELECT * FROM LIKES WHERE USERID = ? AND FILMID = ?";
        SqlRowSet existLike = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        if (!existLike.next()) {
            String setLike = "INSERT INTO LIKES (USERID, FILMID) VALUES (?, ?) ";
            jdbcTemplate.update(setLike, userId, filmId);
        }
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        log.info(String.valueOf(sqlRowSet.next()));
        return sqlRowSet.next();
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String deleteLike = "DELETE FROM LIKES WHERE FILMID = ? AND USERID = ?";
        jdbcTemplate.update(deleteLike, filmId, userId);
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        String sqlMostPopular = "SELECT count(L.LIKEID) AS likeRate" +
                ",FILM.FILMID" +
                ",FILM.NAME ,FILM.DESCRIPTION ,RELEASEDATE ,DURATION ,RATE ,R.RATINGID, R.NAME, R.DESCRIPTION FROM FILM " +
                "LEFT JOIN LIKES L ON L.FILMID = FILM.FILMID " +
                "INNER JOIN RATINGMPA R ON R.RATINGID = FILM.RATINGID " +
                "GROUP BY FILM.FILMID " +
                "ORDER BY likeRate DESC " +
                "limit ?";
        return jdbcTemplate.query(sqlMostPopular, (rs, rowNum) -> makeFilm(rs), count);
    }

    private Film makeFilm(ResultSet resultSet) throws SQLException {
        int filmId = resultSet.getInt("FilmID");
        return new Film(
                filmId,
                resultSet.getString("Name"),
                resultSet.getString("Description"),
                Objects.requireNonNull(resultSet.getDate("ReleaseDate")).toLocalDate(),
                resultSet.getLong("Duration"),
                resultSet.getInt("Rate"),
                new Mpa(resultSet.getInt("RatingMPA.RatingID"),
                        resultSet.getString("RatingMPA.Name"),
                        resultSet.getString("RatingMPA.Description")),
                getFilmLikes(filmId),
                (List<Genre>) genreService.getFilmGenres(filmId));
    }

    private List<Integer> getFilmLikes(int filmId) {
        String sqlGetLikes = "SELECT USERID FROM LIKES WHERE FILMID = ?";
        return jdbcTemplate.queryForList(sqlGetLikes, Integer.class, filmId);
    }
}
