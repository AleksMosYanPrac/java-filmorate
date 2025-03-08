package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.TestFilmData;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.repository.jdbc.FilmStorageImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(FilmStorageImpl.class)
class FilmStorageTest {

    @Autowired
    private FilmStorage filmStorage;

    @Test
    void shouldCreateAndGenerateIdForFilmEntity() throws ExistException {
        Film newFilm = filmStorage.create(TestFilmData.getNewFilm());
        assertThat(newFilm).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Sql("/db/h2/tests/film-test-data.sql")
    void shouldReadAndFindFilmEntityById() throws ExistException {
        Optional<Film> filmById = filmStorage.findById(1L);

        assertThat(filmById).get().hasNoNullFieldsOrProperties().hasFieldOrPropertyWithValue("id", 1L);
        assertThat(filmById.get().getGenres()).isNotEmpty().hasSize(2);
        assertThat(filmById.get().getMpaRating()).hasNoNullFieldsOrProperties();
    }

    @Test
    @Sql("/db/h2/tests/film-test-data.sql")
    void shouldUpdateFilmEntity() throws ExistException {
        Film updated = filmStorage.update(TestFilmData.getFilm());

        assertThat(updated).hasNoNullFieldsOrProperties().hasFieldOrPropertyWithValue("id", 1L);
        assertThat(updated.getMpaRating()).hasNoNullFieldsOrProperties();
        assertThat(updated.getGenres()).containsExactlyElementsOf(TestFilmData.getGenreSet());
    }

    @Test
    @Sql("/db/h2/tests/film-test-data.sql")
    void shouldReadAllFilmEntities() {
        List<Film> list = filmStorage.findAll();
        assertThat(list).hasSize(1);
    }

    @Test
    @Sql("/db/h2/tests/likes-rating-test-data.sql")
    void shouldReadAndSortByRateAndTruncateResultByLimit() {
        List<Film> list = filmStorage.sortByRateAndLimitResult(2);
        assertThat(list).hasSize(2).map(Film::rate).containsSequence(3L, 2L);
    }
}