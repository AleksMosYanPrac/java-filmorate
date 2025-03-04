package ru.yandex.practicum.filmorate.mapping;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.dto.FilmData;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * Test class for the {@link FilmMapper}
 */

@SpringBootTest
public class FilmMapperTest {

    @Autowired
    private FilmMapper filmMapper;

    private Film film;
    private FilmData filmData;
    private FilmInfo filmInfo;

    @BeforeEach
    void initData() {
        LocalDate date = LocalDate.of(2000, 12, 12);
        this.film = Film.builder().id(1L).name("test").description("-")
                .releaseDate(date).duration(10).usersId(Set.of(2L, 3L)).build();
        this.filmData = new FilmData(1L, "test", date, "-", 10);
        this.filmInfo = new FilmInfo(1L, "test", date.format(DateTimeFormatter.ISO_DATE), "-", 10, 2);
    }

    @Test
    void shouldMapFilmToFilmData() {

        Assertions.assertEquals(filmData, filmMapper.toFilmData(film));
    }

    @Test
    void shouldMapFilmToFilmInfo() {

        Assertions.assertEquals(filmInfo, filmMapper.toFilmInfo(film));
    }

    @Test
    void shouldMapFilmDataToFilm() {
        Film expectedFilm = Film.builder().id(1L).name("test").description("-")
                .releaseDate(LocalDate.of(2000, 12, 12)).duration(10).build();

        Assertions.assertEquals(expectedFilm, filmMapper.toFilm(filmData));
    }
}