package ru.yandex.practicum.filmorate.mapping;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.practicum.filmorate.TestFilmData;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.dto.FilmData;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MPARatingService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Test class for the {@link FilmMapper}
 */

@SpringBootTest
public class FilmMapperTest {

    @Autowired
    private FilmMapper filmMapper;

    @MockBean
    private GenreService genreService;

    @MockBean
    private MPARatingService mpaRatingService;

    private Film film;
    private FilmData filmData;
    private FilmInfo filmInfo;

    @BeforeEach
    void initData() throws ExistException {
        this.film = TestFilmData.getFilm();
        this.filmData = TestFilmData.getFilmData();
        this.filmInfo = TestFilmData.getFilmInfo();

        when(mpaRatingService.getMPARatingById(anyLong())).thenReturn(film.getMpaRating());
        when(genreService.getGenresByIdList(anySet())).thenReturn(film.getGenres());
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
    void shouldMapFilmDataToFilm() throws ExistException {
        Film expectedFilm = this.film;

        Assertions.assertEquals(expectedFilm, filmMapper.toFilm(filmData));
    }
}