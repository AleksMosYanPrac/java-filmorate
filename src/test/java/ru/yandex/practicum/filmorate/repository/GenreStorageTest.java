package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.repository.jdbc.GenreStorageImpl;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(GenreStorageImpl.class)
class GenreStorageTest {

    @Autowired
    private GenreStorage genreStorage;

    @Test
    void shouldFindGenresByGivenListId() throws ExistException {
        Set<Long> idsList = Set.of(1L, 2L, 3L, 4L, 5L);
        assertThat(genreStorage.findByIdList(idsList)).hasSize(idsList.size());
    }

    @Test
    void shouldFindGenreById() {
        assertThat(genreStorage.findById(1L).get())
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    void shouldFindAllGenres() {
        int countOFGenres = 6;
        assertThat(genreStorage.findAll()).hasSize(countOFGenres);
    }
}