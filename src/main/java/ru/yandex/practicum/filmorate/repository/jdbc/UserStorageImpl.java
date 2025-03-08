package ru.yandex.practicum.filmorate.repository.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user.Friends;
import ru.yandex.practicum.filmorate.model.user.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserStorageImpl implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        String insertToUsers = "INSERT INTO USERS(LOGIN,EMAIL,NAME,BIRTHDAY) VALUES(?,?,?,?)";
        Long id = jdbcTemplate.execute(psc -> psc.prepareStatement(insertToUsers, Statement.RETURN_GENERATED_KEYS),
                (PreparedStatementCallback<Long>) users -> {
                    users.setString(1, user.getLogin());
                    users.setString(2, user.getEmail());
                    users.setString(3, user.getName());
                    users.setDate(4, Date.valueOf(user.getBirthday()));
                    users.executeUpdate();
                    ResultSet rs = users.getGeneratedKeys();
                    if (rs.next()) {
                        return rs.getLong("ID");
                    }
                    return null;
                });
        log.info("New user has been created with ID: {}", id);
        return user.toBuilder().id(id).build();
    }

    @Override
    public Optional<User> findById(long id) {
        String selectFromUsers = "SELECT ID,LOGIN,EMAIL,NAME,BIRTHDAY FROM USERS WHERE ID = ?";
        User user = jdbcTemplate.execute(selectFromUsers, (PreparedStatementCallback<User>) users -> {
            users.setLong(1, id);
            ResultSet rs = users.executeQuery();
            if (rs.next()) {
                return createUser(rs, getFriends(id));
            }
            return null;
        });
        return Optional.ofNullable(user);
    }

    @Override
    public User update(User user) {
        String updateUsers = "UPDATE USERS SET LOGIN=?,EMAIL=?,NAME=?,BIRTHDAY=? WHERE ID=?";
        jdbcTemplate.execute(updateUsers, (PreparedStatementCallback<Void>) users -> {
            users.setString(1, user.getLogin());
            users.setString(2, user.getEmail());
            users.setString(3, user.getName());
            users.setDate(4, Date.valueOf(user.getBirthday()));
            users.setLong(5, user.getId());
            users.executeUpdate();
            return null;
        });
        log.info("User with id:{} has been updated", user.getId());
        return user;
    }

    @Override
    public List<User> findAll() {
        String selectAllFromUsers = "SELECT ID,LOGIN,EMAIL,NAME,BIRTHDAY FROM USERS";
        return jdbcTemplate.execute(selectAllFromUsers, (PreparedStatementCallback<List<User>>) users -> {
            ResultSet rs = users.executeQuery();
            List<User> usersList = new ArrayList<>();
            while (rs.next()) {
                usersList.add(createUser(rs, getFriends(rs.getLong("ID"))));
            }
            return usersList;
        });
    }

    @Override
    public List<User> findFriendsForUserById(long userId) {
        String selectUserFriends = """
                SELECT ID,LOGIN,EMAIL,NAME,BIRTHDAY FROM USERS
                WHERE ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?)
                """;
        return jdbcTemplate.execute(selectUserFriends, (PreparedStatementCallback<List<User>>) friends -> {
            friends.setLong(1, userId);
            ResultSet rs = friends.executeQuery();
            List<User> usersList = new ArrayList<>();
            while (rs.next()) {
                usersList.add(createUser(rs, getFriends(rs.getLong("ID"))));
            }
            return usersList;
        });
    }

    @Override
    public List<User> findCommonFriendsForUsersById(long userId, long otherUserId) {
        String selectCommonFriends = """
                SELECT ID,LOGIN,EMAIL,NAME,BIRTHDAY FROM USERS
                WHERE ID IN (
                SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?
                INTERSECT
                SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?
                )
                """;
        return jdbcTemplate.execute(selectCommonFriends, (PreparedStatementCallback<List<User>>) friends -> {
            friends.setLong(1, userId);
            friends.setLong(2, otherUserId);
            ResultSet rs = friends.executeQuery();
            List<User> usersList = new ArrayList<>();
            while (rs.next()) {
                usersList.add(createUser(rs, getFriends(rs.getLong("ID"))));
            }
            return usersList;
        });
    }

    private User createUser(ResultSet rs, Friends friends) throws SQLException {
        return User.builder()
                .id(rs.getLong("ID"))
                .login(rs.getString("LOGIN"))
                .email(rs.getString("EMAIL"))
                .name(rs.getString("NAME"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                .friends(friends)
                .build();
    }

    private Friends getFriends(long id) {
        String selectFriends = "SELECT FRIEND_ID,STATUS FROM FRIENDS WHERE USER_ID = ?";
        Map<Long, FriendshipStatus> friendshipStatusMap = jdbcTemplate.execute(selectFriends,
                (PreparedStatementCallback<Map<Long, FriendshipStatus>>) friends -> {
                    friends.setLong(1, id);
                    ResultSet rs = friends.executeQuery();
                    Map<Long, FriendshipStatus> result = new HashMap<>();
                    while (rs.next()) {
                        result.put(rs.getLong("FRIEND_ID"),
                                FriendshipStatus.valueOf(rs.getString("STATUS")));
                    }
                    return result;
                });
        return new Friends(id, friendshipStatusMap);
    }
}