package ru.yandex.practicum.filmorate.service.impls;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.RateService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test class for the {@link RateService}
 */

@SpringBootTest
public class RateServiceTest {

    @Autowired
    private RateService rateService;

    @MockBean
    private UserStorage userStorage;

    @MockBean
    private FilmStorage filmStorage;

    private User user;
    private Film film;

    @BeforeEach
    void init() throws ExistException {
        this.user = User.builder().id(1L).login("1").email("e@x.com").birthday(LocalDate.now()).build();
        this.film = Film.builder().id(1L).name("ap").duration(100L).releaseDate(LocalDate.now()).build();

        when(userStorage.findById(user.getId())).thenReturn(user);
        when(filmStorage.findById(film.getId())).thenReturn(film);
    }

    @Test
    void shouldPutLikeFromUserToFilm() throws ExistException {

        rateService.putLike(film.getId(),user.getId());

        assertThat(film.rate()).isEqualTo(1L);
    }

    @Test
    void shouldRemoveLikeFromUser() throws ExistException {
        rateService.putLike(film.getId(),user.getId());

        rateService.removeLike(film.getId(),user.getId());

        assertThat(film.rate()).isEqualTo(0);
    }
}