package ru.yandex.practicum.filmorate.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.LikesRating;
import ru.yandex.practicum.filmorate.repository.LikesRatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class LikesRatingStorageImpl implements LikesRatingStorage {

    private final JdbcClient jdbcClient;

    @Override
    public LikesRating findById(Long filmId) {
        return jdbcClient.sql("SELECT USER_ID FROM LIKES_RATING WHERE FILM_ID=:film_id")
                .param("film_id", filmId)
                .query(new LikesRatingExtractor(filmId));
    }

    @Override
    public LikesRating save(LikesRating rating) {
        String sql = """
                MERGE INTO LIKES_RATING AS T
                USING (
                    SELECT * FROM (
                        (SELECT * FROM LIKES_RATING WHERE FILM_ID=:film_id
                        UNION
                        SELECT * FROM (SELECT * FROM (VALUES %s) AS VA(FILM_ID,USER_ID))
                        )
                    AS SO
                    LEFT JOIN (SELECT * FROM (VALUES %s)) AS V(F_ID,U_ID) ON SO.USER_ID = V.U_ID)
                )
                AS S(FILM_ID,USER_ID,F_ID_UP,U_ID_UP)
                ON (T.FILM_ID = S.FILM_ID AND T.USER_ID = S.USER_ID)
                WHEN MATCHED AND S.U_ID_UP IS NULL THEN DELETE
                WHEN NOT MATCHED AND S.U_ID_UP IS NOT NULL THEN INSERT VALUES(S.FILM_ID,S.USER_ID)
                """;
        String values = formatValues(rating);
        jdbcClient.sql(String.format(sql, values, values))
                .param("film_id", rating.getFilmId())
                .update();
        return rating;
    }

    private String formatValues(LikesRating rating) {
        if (rating.getUsersId().isEmpty()) {
            return String.format("(%d,%s)", rating.getFilmId(), null);
        }
        return rating.getUsersId()
                .stream()
                .map(e -> String.format("(%d,%d)", rating.getFilmId(), e))
                .collect(Collectors.joining(","));
    }

    private record LikesRatingExtractor(Long filmId) implements ResultSetExtractor<LikesRating> {
        @Override
        public LikesRating extractData(ResultSet rs) throws SQLException, DataAccessException {
            Set<Long> usersId = new HashSet<>();
            while (rs.next()) {
                usersId.add(rs.getLong("USER_ID"));
            }
            return new LikesRating(filmId, usersId);
        }
    }
}