package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
@Primary
public class DBUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public DBUserStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUser(Integer id) {
        String sqlUser = "select * from Users where UserID = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sqlUser, (rs, rowNum) -> makeUser(rs), id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    id + " не зарегистрирован!");
        }
        return user;
    }

    @Override
    public Collection<User> findAllUsers() {
        String sqlAllUsers = "select * from Users";
        return jdbcTemplate.query(sqlAllUsers, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "insert into Users " +
                "(EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));

            return preparedStatement;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        if (user.getFriends() != null) {
            for (Integer friendId : user.getFriends()) {
                addFriend(user.getId(), friendId);
            }
        }
        return getUser(id);
    }

    @Override
    public User updateUser(User user) {
        String sqlUser = "update Users set " +
                "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "where UserID = ?";
        jdbcTemplate.update(sqlUser,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());

        return getUser(user.getId());
    }

    @Override
    public void deleteUser(User user) {
        String sqlQuery = "delete from Users where UserID = ?";
        //return jdbcTemplate.update(sqlQuery, user.getId()) > 0;
    }

    private User makeUser(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("UserID");
        return new User(
                userId,
                resultSet.getString("Email"),
                resultSet.getString("Login"),
                resultSet.getString("Name"),
                Objects.requireNonNull(resultSet.getDate("BirthDay")).toLocalDate(),
                getUserFriends(userId));
    }

    private Set<Integer> getUserFriends(int userId) {
        String sqlGetFriends = "select FriendID from Friends where UserID = ?";
        return new HashSet<>(jdbcTemplate.queryForList (sqlGetFriends, Integer.class, userId));
    }

    @Override
    public boolean addFriend(int userId, int friendId) {
        boolean friendAccepted;
        String sqlGetReversFriend = "select * from Friends " +
                "where UserID = ? and FriendID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlGetReversFriend, friendId, userId);
        friendAccepted = sqlRowSet.next();
        String sqlSetFriend = "insert into Friends (UserID, FriendID, Confirmed) " +
                "VALUES (?,?,?)";
        jdbcTemplate.update(sqlSetFriend, userId, friendId, friendAccepted);
        if (friendAccepted) {
            String sqlSetStatus = "update Friends set Confirmed = true " +
                    "where UserID = ? and FriendID = ?";
            jdbcTemplate.update(sqlSetStatus, friendId, userId);
        }
        return true;
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlDeleteFriend = "delete from Friends where UserID = ? and FriendID = ?";
        jdbcTemplate.update(sqlDeleteFriend, userId, friendId);
        String sqlSetStatus = "update Friends set Confirmed = false " +
                "where UserID = ? and FriendID = ?";
        jdbcTemplate.update(sqlSetStatus, friendId, userId);
        //return true;
    }

}
