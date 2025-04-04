package ru.yandex.practicum.filmorate.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user.Friends;
import ru.yandex.practicum.filmorate.model.user.FriendshipStatus;
import ru.yandex.practicum.filmorate.repository.FriendsStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FriendsStorageImpl implements FriendsStorage {

    private final JdbcClient jdbcClient;

    @Override
    public Friends findById(Long userId) {
        return jdbcClient.sql("SELECT FRIEND_ID,STATUS FROM FRIENDS WHERE USER_ID=:user_id")
                .param("user_id", userId)
                .query(new FriendsExtractor(userId));
    }

    @Override
    public Friends save(Friends friends) {
        String sql = """
                MERGE INTO FRIENDS AS T
                USING (
                    SELECT USER_ID,FRIEND_ID,NEW_STATUS FROM (
                        (SELECT USER_ID,FRIEND_ID FROM FRIENDS WHERE USER_ID = :user_id
                        UNION
                        SELECT USER_ID,FRIEND_ID FROM (SELECT * FROM (VALUES %s) AS NV(USER_ID,FRIEND_ID,STATUS))
                        )
                    AS SO
                    LEFT JOIN (SELECT * FROM (VALUES %s)) AS V(U,F,NEW_STATUS) ON SO.FRIEND_ID = V.F)
                )
                AS S(USER_ID,FRIEND_ID,NEW_STATUS)
                ON (T.USER_ID=S.USER_ID AND T.FRIEND_ID = S.FRIEND_ID)
                WHEN MATCHED AND S.NEW_STATUS IS NULL THEN
                DELETE
                WHEN MATCHED AND (S.NEW_STATUS IS NOT NULL AND S.NEW_STATUS <> T.STATUS) THEN
                UPDATE SET T.STATUS = S.NEW_STATUS
                WHEN NOT MATCHED AND S.FRIEND_ID IS NOT NULL THEN
                INSERT VALUES(S.USER_ID,S.FRIEND_ID,S.NEW_STATUS)
                """;
        String values = formatValues(friends);
        jdbcClient.sql(String.format(sql, values, values))
                .param("user_id", friends.getUserId())
                .update();
        return friends;
    }

    private String formatValues(Friends friends) {
        if (friends.getFriendsId().isEmpty()) {
            return String.format("(%d,%s,%s)", friends.getUserId(), null, null);
        }
        return friends.getFriendIdAndStatus()
                .entrySet()
                .stream()
                .map(e -> String.format("(%d,%d,'%s')", friends.getUserId(), e.getKey(), e.getValue().toString()))
                .collect(Collectors.joining(","));
    }

    private record FriendsExtractor(Long userId) implements ResultSetExtractor<Friends> {

        @Override
        public Friends extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, FriendshipStatus> friendshipStatusMap = new HashMap<>();
            while (rs.next()) {
                friendshipStatusMap.put(rs.getLong("FRIEND_ID"),
                        FriendshipStatus.valueOf(rs.getString("STATUS")));
            }
            return new Friends(userId, friendshipStatusMap);
        }
    }
}