package ru.yandex.practicum.filmorate.repository.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.MPARating;
import ru.yandex.practicum.filmorate.model.film.LikesRating;
import ru.yandex.practicum.filmorate.repository.FilmStorage;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmStorageImpl implements FilmStorage {

    private final JdbcClient jdbcClient;

    @Override
    public Film create(Film film) {
        String insertToFilms = "INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING) " +
                               "VALUES (:name, :description, :release, :duration, :mpa_id)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(insertToFilms)
                .param("name", film.getName())
                .param("description", film.getDescription())
                .param("release", film.getReleaseDate())
                .param("duration", film.getDuration())
                .param("mpa_id", film.getMpaRating().getId())
                .update(keyHolder);
        updateFilmGenreTable(film.getGenres(), keyHolder.getKeyAs(Long.class));
        log.info("Film created with ID:{}", keyHolder.getKey());
        return film.toBuilder().id(keyHolder.getKeyAs(Long.class)).build();
    }

    @Override
    public Optional<Film> findById(long id) {
        String selectFromFilms = """
                SELECT F.ID,F.NAME,F.DESCRIPTION,F.RELEASE_DATE,F.DURATION,MPA.ID AS MPA_ID,MPA.NAME AS MPA_NAME,
                G.ID AS GENRE_ID, G.NAME AS GENRE_NAME,LR.USER_ID AS USER_ID
                FROM FILMS F
                LEFT JOIN MPA_RATING MPA ON F.MPA_RATING=MPA.ID
                LEFT JOIN FILM_GENRE FG ON F.ID=FG.FILM_ID
                LEFT JOIN GENRES G ON FG.GENRE_ID = G.ID
                LEFT JOIN LIKES_RATING LR ON F.ID=LR.FILM_ID
                WHERE F.ID = :film_id
                """;
        return jdbcClient.sql(selectFromFilms)
                .param("film_id", id)
                .query(new FilmExtractor())
                .stream()
                .findFirst();
    }

    @Override
    public Film update(Film film) {
        String updateFilms = """
                UPDATE FILMS SET NAME= :name, DESCRIPTION = :description, RELEASE_DATE = :release,
                DURATION = :duration, MPA_RATING = :mpa_id
                WHERE ID = :film_id
                """;
        jdbcClient.sql(updateFilms)
                .param("film_id", film.getId())
                .param("name", film.getName())
                .param("description", film.getDescription())
                .param("release", film.getReleaseDate())
                .param("duration", film.getDuration())
                .param("mpa_id", film.getMpaRating().getId())
                .update();
        log.info("Film updated with ID:{}", film.getId());
        return film;
    }

    @Override
    public List<Film> findAll() {
        String selectFromFilms = """
                SELECT F.ID,F.NAME,F.DESCRIPTION,F.RELEASE_DATE,F.DURATION,MPA.ID AS MPA_ID,MPA.NAME AS MPA_NAME,
                G.ID AS GENRE_ID, G.NAME AS GENRE_NAME,LR.USER_ID AS USER_ID
                FROM FILMS F
                LEFT JOIN MPA_RATING MPA ON F.MPA_RATING=MPA.ID
                LEFT JOIN FILM_GENRE FG ON F.ID=FG.FILM_ID
                LEFT JOIN GENRES G ON FG.GENRE_ID = G.ID
                LEFT JOIN LIKES_RATING LR ON F.ID=LR.FILM_ID
                """;
        return jdbcClient.sql(selectFromFilms)
                .query(new FilmExtractor())
                .stream()
                .toList();
    }

    @Override
    public List<Film> sortByRateAndLimitResult(long listSize) {
        String selectFromFilms = """
                SELECT F.ID,F.NAME,F.DESCRIPTION,F.RELEASE_DATE,F.DURATION,MPA.ID AS MPA_ID,MPA.NAME AS MPA_NAME,
                G.ID AS GENRE_ID, G.NAME AS GENRE_NAME,LR.USER_ID AS USER_ID
                FROM ( SELECT  COUNT(LR.USER_ID) AS LIKES, F.ID
                       FROM  FILMS F
                       LEFT JOIN LIKES_RATING LR ON F.ID=LR.FILM_ID
                       GROUP BY F.ID
                       ORDER BY LIKES DESC
                       LIMIT :listSize) AS R(LIKES,ID)
                LEFT JOIN FILMS F ON R.ID=F.ID
                LEFT JOIN MPA_RATING MPA ON F.MPA_RATING=MPA.ID
                LEFT JOIN FILM_GENRE FG ON F.ID=FG.FILM_ID
                LEFT JOIN GENRES G ON FG.GENRE_ID = G.ID
                LEFT JOIN LIKES_RATING LR ON F.ID=LR.FILM_ID
                """;
        return jdbcClient.sql(selectFromFilms)
                .param("listSize", listSize)
                .query(new FilmExtractor())
                .stream()
                .sorted(Comparator.comparingLong(Film::rate).reversed())
                .toList();
    }

    @Override
    public boolean exist(long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM FILMS WHERE ID =:film_id)";
        return jdbcClient.sql(sql)
                .param("film_id", id)
                .query(Boolean.class)
                .single();
    }

    private void updateFilmGenreTable(Set<Genre> genres, Long filmId) {
        String updateFilmGenre = """
                MERGE INTO FILM_GENRE AS T
                USING (
                    SELECT FILM_ID, UPDATED, OLDEST FROM FILM_GENRE fg
                    RIGHT JOIN (SELECT ID,UPDATED,OLDEST FROM GENRES fgg
                        LEFT JOIN (SELECT ID AS UPDATED FROM GENRES WHERE ID IN (%s))
                        ON fgg.ID = UPDATED
                    LEFT JOIN (SELECT FILM_ID,GENRE_ID AS OLDEST FROM FILM_GENRE WHERE FILM_ID= :film_id)
                        ON fgg.ID = OLDEST)
                    ON (fg.FILM_ID = :film_id AND fg.GENRE_ID = ID) WHERE (UPDATED IS NOT NULL OR OLDEST IS NOT NULL)
                ) AS S
                ON (T.FILM_ID = S.FILM_ID AND T.GENRE_ID = S.OLDEST)
                WHEN MATCHED AND S.UPDATED IS NULL THEN DELETE
                WHEN NOT MATCHED THEN INSERT VALUES(:film_id, S.UPDATED)
                """;
        jdbcClient.sql(String.format(updateFilmGenre,
                        genres.stream().map(g -> g.getId().toString()).collect(Collectors.joining(","))))
                .param("film_id", filmId)
                .update();
    }

    private static class FilmExtractor implements ResultSetExtractor<Collection<Film>> {

        @Override
        public Collection<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Film> films = new HashMap<>();
            while (rs.next()) {
                Set<Long> usersId = new HashSet<>();
                Set<Genre> filmGenres = new HashSet<>();
                if (rs.getLong("USER_ID") != 0) {
                    usersId.add(rs.getLong("USER_ID"));
                }
                if (rs.getLong("GENRE_ID") != 0) {
                    filmGenres.add(createGenre(rs));
                }
                Film film = createFilm(rs, filmGenres, usersId);
                if (films.containsKey(film.getId())) {
                    films.computeIfPresent(film.getId(), (id, f) -> {
                        f.getGenres().addAll(filmGenres);
                        f.getLikesRating().getUsersId().addAll(usersId);
                        return f;
                    });
                } else {
                    films.put(film.getId(), film);
                }
            }
            return films.values();
        }

        private Genre createGenre(ResultSet rs) throws SQLException {
            return new Genre(rs.getLong("GENRE_ID"), rs.getString("GENRE_NAME"));
        }

        private Film createFilm(ResultSet rs, Set<Genre> genres, Set<Long> usersId) throws SQLException {
            return Film.builder()
                    .id(rs.getLong("ID"))
                    .name(rs.getString("NAME"))
                    .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                    .description(rs.getString("DESCRIPTION"))
                    .duration(rs.getLong("DURATION"))
                    .genres(genres)
                    .likesRating(new LikesRating(rs.getLong("ID"), usersId))
                    .mpaRating(new MPARating(rs.getLong("MPA_ID"), rs.getString("MPA_NAME")))
                    .build();
        }
    }
}