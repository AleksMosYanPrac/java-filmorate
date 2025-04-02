package ru.yandex.practicum.filmorate.repository.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user.Friends;
import ru.yandex.practicum.filmorate.model.user.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserStorageImpl implements UserStorage {

    private final JdbcClient jdbcClient;

    @Override
    public User create(User user) {
        String insertToUsers = "INSERT INTO USERS(LOGIN,EMAIL,NAME,BIRTHDAY) VALUES(:login,:email,:name,:birthday)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(insertToUsers)
                .param("login", user.getLogin())
                .param("email", user.getEmail())
                .param("name", user.getName())
                .param("birthday", user.getBirthday())
                .update(keyHolder);
        log.info("New user has been created with ID: {}", keyHolder.getKey());
        return user.toBuilder().id(keyHolder.getKeyAs(Long.class)).build();
    }

    @Override
    public Optional<User> findById(long id) {
        String selectFromUsers = """
                SELECT ID,LOGIN,EMAIL,NAME,BIRTHDAY,FRIEND_ID,STATUS
                FROM USERS U
                LEFT JOIN FRIENDS F ON U.ID=F.USER_ID
                WHERE ID = :id
                """;
        return jdbcClient.sql(selectFromUsers)
                .param("id", id)
                .query(new UserExtractor())
                .stream()
                .findFirst();
    }

    @Override
    public User update(User user) {
        String updateUsers = "UPDATE USERS SET LOGIN=:login,EMAIL=:email,NAME=:name,BIRTHDAY=:birthday WHERE ID=:id";
        jdbcClient.sql(updateUsers)
                .param("id", user.getId())
                .param("login", user.getLogin())
                .param("email", user.getEmail())
                .param("name", user.getName())
                .param("birthday", user.getBirthday())
                .update();
        log.info("User with id:{} has been updated", user.getId());
        return user;
    }

    @Override
    public List<User> findAll() {
        String selectAllFromUsers = """
                SELECT ID,LOGIN,EMAIL,NAME,BIRTHDAY,FRIEND_ID,STATUS
                FROM USERS U
                LEFT JOIN FRIENDS F ON U.ID=F.USER_ID
                """;
        return jdbcClient.sql(selectAllFromUsers)
                .query(new UserExtractor())
                .stream().sorted(Comparator.comparingLong(User::getId))
                .toList();
    }

    @Override
    public List<User> findFriendsForUserById(long userId) {
        String selectUserFriends = """
                SELECT ID,LOGIN,EMAIL,NAME,BIRTHDAY,FRIEND_ID,STATUS
                FROM USERS U
                LEFT JOIN FRIENDS F ON U.ID=F.USER_ID
                WHERE ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = :id)
                """;
        return jdbcClient.sql(selectUserFriends)
                .param("id", userId)
                .query(new UserExtractor())
                .stream()
                .toList();
    }

    @Override
    public List<User> findCommonFriendsForUsersById(long userId, long otherUserId) {
        String selectCommonFriends = """
                SELECT ID,LOGIN,EMAIL,NAME,BIRTHDAY,FRIEND_ID,STATUS
                FROM USERS U
                LEFT JOIN FRIENDS F ON U.ID=F.USER_ID
                WHERE ID IN (
                SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = :user_id
                INTERSECT
                SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = :other_user_id
                )
                """;
        return jdbcClient.sql(selectCommonFriends)
                .param("user_id", userId)
                .param("other_user_id", otherUserId)
                .query(new UserExtractor())
                .stream()
                .toList();
    }

    @Override
    public boolean exist(long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM USERS WHERE ID = :user_id)";
        return jdbcClient.sql(sql)
                .param("user_id", id)
                .query(Boolean.class)
                .single();
    }

    private static class UserExtractor implements ResultSetExtractor<Collection<User>> {

        @Override
        public Collection<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, User> users = new HashMap<>();
            while (rs.next()) {
                Map<Long, FriendshipStatus> friends = new HashMap<>();
                if (rs.getLong("FRIEND_ID") != 0) {
                    friends.put(rs.getLong("FRIEND_ID"), FriendshipStatus.valueOf(rs.getString("STATUS")));
                }
                User user = createUser(rs, friends);
                if (users.containsKey(user.getId())) {
                    users.computeIfPresent(user.getId(), (k, v) -> {
                        v.getFriends().getFriendIdAndStatus().putAll(friends);
                        return v;
                    });
                } else {
                    users.put(user.getId(), user);
                }
            }
            return users.values();
        }

        private User createUser(ResultSet rs, Map<Long, FriendshipStatus> friendshipStatusMap) throws SQLException {
            return User.builder()
                    .id(rs.getLong("ID"))
                    .login(rs.getString("LOGIN"))
                    .email(rs.getString("EMAIL"))
                    .name(rs.getString("NAME"))
                    .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                    .friends(new Friends(rs.getLong("ID"), friendshipStatusMap))
                    .build();
        }
    }
}