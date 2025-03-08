package ru.yandex.practicum.filmorate.repository.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.MPARating;
import ru.yandex.practicum.filmorate.model.film.LikesRating;
import ru.yandex.practicum.filmorate.repository.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class FilmStorageImpl implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        String insertToFilmGenre = "INSERT INTO FILM_GENRE(FILM_ID,GENRE_ID) VALUES (?, ?)";
        String insertToFilms = "INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING) " +
                               "VALUES (?, ?, ?, ?, ?)";
        Long filmId = jdbcTemplate.execute(insertToFilmGenre, (PreparedStatementCallback<Long>) filmGenre ->
                jdbcTemplate.execute(psc -> psc.prepareStatement(insertToFilms, Statement.RETURN_GENERATED_KEYS),
                        (PreparedStatementCallback<Long>) films -> {
                            films.setString(1, film.getName());
                            films.setString(2, film.getDescription());
                            films.setDate(3, Date.valueOf(film.getReleaseDate()));
                            films.setLong(4, film.getDuration());
                            films.setLong(5, film.getMpaRating().getId());
                            films.executeUpdate();
                            ResultSet rs = films.getGeneratedKeys();
                            if (rs.next()) {
                                long id = rs.getLong("ID");
                                updateFilmGenreTable(film.getGenres(), id);
                                return id;
                            }
                            return null;
                        }));
        log.info("Film created with ID:{}", filmId);
        return film.toBuilder().id(filmId).build();
    }

    @Override
    public Optional<Film> findById(long id) {
        String selectFromFilms = """
                SELECT F.ID,F.NAME,F.DESCRIPTION,F.RELEASE_DATE,F.DURATION,MPA.ID AS MPA_ID,MPA.NAME AS MPA_NAME
                FROM FILMS F
                LEFT JOIN MPA_RATING MPA ON F.MPA_RATING=MPA.ID
                WHERE F.ID = ?
                """;
        Film film = jdbcTemplate.execute(selectFromFilms, (PreparedStatementCallback<Film>) films -> {
            films.setLong(1, id);
            ResultSet rs = films.executeQuery();
            if (rs.next()) {
                return createFilm(rs, getGenres(id), getLikesRating(id));
            }
            return null;
        });
        return Optional.ofNullable(film);
    }

    @Override
    public Film update(Film film) {
        String updateFilms = """
                UPDATE FILMS SET NAME= ?,DESCRIPTION = ?,RELEASE_DATE = ?,DURATION = ?,MPA_RATING = ?
                WHERE ID = ?
                """;
        jdbcTemplate.execute(updateFilms, (PreparedStatementCallback<Void>) films -> {
            films.setString(1, film.getName());
            films.setString(2, film.getDescription());
            films.setDate(3, Date.valueOf(film.getReleaseDate()));
            films.setLong(4, film.getDuration());
            films.setLong(5, film.getMpaRating().getId());
            films.setLong(6, film.getId());
            films.executeUpdate();
            updateFilmGenreTable(film.getGenres(), film.getId());
            return null;
        });
        log.info("Film updated with ID:{}", film.getId());
        return film;
    }

    @Override
    public List<Film> findAll() {
        String selectFromFilms = """
                SELECT F.ID,F.NAME,F.DESCRIPTION,F.RELEASE_DATE,F.DURATION,MPA.ID AS MPA_ID,MPA.NAME AS MPA_NAME
                FROM FILMS F
                LEFT JOIN MPA_RATING MPA ON F.MPA_RATING=MPA.ID
                """;
        return jdbcTemplate.execute(selectFromFilms, (PreparedStatementCallback<List<Film>>) films -> {
            ResultSet rs = films.executeQuery();
            List<Film> filmsList = new ArrayList<>();
            while (rs.next()) {
                Long id = rs.getLong("ID");
                filmsList.add(createFilm(rs, getGenres(id), getLikesRating(id)));
            }
            return filmsList;
        });
    }

    @Override
    public List<Film> sortByRateAndLimitResult(long listSize) {
        String selectFromFilms = """
                SELECT F.ID,F.NAME,F.DESCRIPTION,F.RELEASE_DATE,F.DURATION,MPA.ID AS MPA_ID,MPA.NAME AS MPA_NAME,
                COUNT(LR.USER_ID) AS LIKES
                FROM FILMS F
                LEFT JOIN MPA_RATING MPA ON F.MPA_RATING=MPA.ID
                LEFT JOIN LIKES_RATING LR ON F.ID=LR.FILM_ID
                GROUP BY F.ID
                ORDER BY LIKES DESC
                LIMIT ?
                """;
        return jdbcTemplate.execute(selectFromFilms, (PreparedStatementCallback<List<Film>>) films -> {
            films.setLong(1, listSize);
            ResultSet rs = films.executeQuery();
            List<Film> filmsList = new ArrayList<>();
            while (rs.next()) {
                Long id = rs.getLong("ID");
                filmsList.add(createFilm(rs, getGenres(id), getLikesRating(id)));
            }
            return filmsList;
        });
    }

    private void updateFilmGenreTable(Set<Genre> genres, Long filmId) {
        String updateFilmGenre = """
                MERGE INTO FILM_GENRE AS T
                USING (
                    SELECT FILM_ID, UPDATED, OLDEST FROM FILM_GENRE fg
                    RIGHT JOIN (SELECT ID,UPDATED,OLDEST FROM GENRES fgg
                        LEFT JOIN (SELECT ID AS UPDATED FROM GENRES WHERE ID IN (%s))
                        ON fgg.ID = UPDATED
                    LEFT JOIN (SELECT FILM_ID,GENRE_ID AS OLDEST FROM FILM_GENRE WHERE FILM_ID= ?)
                        ON fgg.ID = OLDEST)
                    ON (fg.FILM_ID = ? AND fg.GENRE_ID = ID) WHERE (UPDATED IS NOT NULL OR OLDEST IS NOT NULL)
                ) AS S
                ON (T.FILM_ID = S.FILM_ID AND T.GENRE_ID = S.OLDEST)
                WHEN MATCHED AND S.UPDATED IS NULL THEN DELETE
                WHEN NOT MATCHED THEN INSERT VALUES(?, S.UPDATED)
                """;
        jdbcTemplate.execute(String.format(updateFilmGenre,
                        genres.stream().map(g -> g.getId().toString()).collect(Collectors.joining(","))),
                (PreparedStatementCallback<Void>) filmGenre -> {
                    filmGenre.setLong(1, filmId);
                    filmGenre.setLong(2, filmId);
                    filmGenre.setLong(3, filmId);
                    filmGenre.executeUpdate();
                    return null;
                });
    }

    private Set<Genre> getGenres(Long filmId) {
        String selectFromGenres = "SELECT ID,NAME FROM GENRES WHERE ID IN " +
                                  "(SELECT GENRE_ID FROM FILM_GENRE WHERE FILM_ID = ?)";
        return jdbcTemplate.execute(selectFromGenres, (PreparedStatementCallback<Set<Genre>>) genres -> {
            genres.setLong(1, filmId);
            return createGenres(genres.executeQuery());
        });
    }

    private LikesRating getLikesRating(Long filmId) {
        String selectFromLikesRating = "SELECT USER_ID FROM LIKES_RATING WHERE FILM_ID = ?";
        return jdbcTemplate.execute(selectFromLikesRating, (PreparedStatementCallback<LikesRating>) likesRating -> {
            likesRating.setLong(1, filmId);
            return createLikesRating(filmId, likesRating.executeQuery());
        });
    }

    private Film createFilm(ResultSet filmsSet, Set<Genre> genres, LikesRating likesRating) throws SQLException {
        return Film.builder()
                .id(filmsSet.getLong("ID"))
                .name(filmsSet.getString("NAME"))
                .releaseDate(filmsSet.getDate("RELEASE_DATE").toLocalDate())
                .description(filmsSet.getString("DESCRIPTION"))
                .duration(filmsSet.getLong("DURATION"))
                .genres(genres)
                .likesRating(likesRating)
                .mpaRating(new MPARating(filmsSet.getLong("MPA_ID"), filmsSet.getString("MPA_NAME")))
                .build();
    }

    private Set<Genre> createGenres(ResultSet filmGenreSet) throws SQLException {
        Set<Genre> genres = new HashSet<>();
        while (filmGenreSet.next()) {
            genres.add(new Genre(
                    filmGenreSet.getLong("ID"),
                    filmGenreSet.getString("NAME"))
            );
        }
        return genres;
    }

    private LikesRating createLikesRating(Long filmId, ResultSet likesRating) throws SQLException {
        Set<Long> usersId = new HashSet<>();
        while (likesRating.next()) {
            usersId.add(likesRating.getLong("USER_ID"));
        }
        return new LikesRating(filmId,usersId);
    }
}