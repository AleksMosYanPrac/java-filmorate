package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.repository.jdbc.MPARatingStorageImpl;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(MPARatingStorageImpl.class)
class MPARatingStorageTest {

    @Autowired
    private MPARatingStorage mpaRatingStorage;

    @Test
    void shouldFindRatingById() {
        assertThat(mpaRatingStorage.findById(1L).get())
                .hasFieldOrPropertyWithValue("id",1L)
                .hasFieldOrPropertyWithValue("name","G");
    }

    @Test
    void shouldFindAllMPARatings() {
        int countOfMPARatings = 5;
        assertThat(mpaRatingStorage.findAll()).hasSize(countOfMPARatings);
    }
}