package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.film.LikesRating;
import ru.yandex.practicum.filmorate.repository.jdbc.LikesRatingStorageImpl;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(LikesRatingStorageImpl.class)
class LikesRatingStorageTest {

    @Autowired
    private LikesRatingStorage likesRatingStorage;

    @Test
    @Sql("/db/h2/tests/likes-rating-test-data.sql")
    void shouldFindLikeRatingEntityByProvidedFilmIdWhenTableIsNotEmpty() throws ExistException {
        assertThat(likesRatingStorage.findById(1L))
                .isNotNull()
                .hasFieldOrPropertyWithValue("filmId", 1L)
                .satisfies(l -> assertThat(l.getUsersId()).containsExactly(1L, 2L, 3L));
    }

    @Test
    @Sql("/db/h2/tests/film-test-data.sql")
    @Sql("/db/h2/tests/user-test-data.sql")
    void shouldFindLikeRatingEntityByProvidedFilmIdWhenEmptyTable() throws ExistException {
        assertThat(likesRatingStorage.findById(1L))
                .isNotNull()
                .hasFieldOrPropertyWithValue("filmId", 1L)
                .satisfies(l -> assertThat(l.getUsersId()).isEmpty());
    }

    @Test
    @Sql("/db/h2/tests/film-test-data.sql")
    @Sql("/db/h2/tests/user-test-data.sql")
    void shouldSaveNewLikesRatingEntity() throws ExistException {
        LikesRating likesRating = new LikesRating(1L, new HashSet<>());
        likesRating.addLike(2L);
        likesRating.addLike(3L);
        likesRatingStorage.save(likesRating);

        assertThat(likesRatingStorage.findById(1L).getUsersId()).containsExactly(2L, 3L);
    }

    @Test
    @Sql("/db/h2/tests/likes-rating-test-data.sql")
    void shouldSaveUpdatedELikesRatingEntity() throws ExistException {
        LikesRating likesRating = likesRatingStorage.findById(1L);
        likesRating.deleteLike(3L);
        likesRatingStorage.save(likesRating);

        assertThat(likesRatingStorage.findById(1L).getUsersId()).containsExactly(1L, 2L);
    }
}