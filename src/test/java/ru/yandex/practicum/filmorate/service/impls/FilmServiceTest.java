package ru.yandex.practicum.filmorate.service.impls;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.dto.FilmData;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link FilmService}
 */

@SpringBootTest
class FilmServiceTest {

    @Autowired
    private FilmService filmService;

    @Test
    void shouldAddFilmBySettingLongID() throws ExistException {
        FilmData newFilm = new FilmData(null, "a", LocalDate.now(), "-", 10L);
        FilmData addedFilm = filmService.add(newFilm);

        assertAll("Check Long id before and after add",
                () -> assertTrue(Objects.isNull(newFilm.getId())),
                () -> assertTrue(Objects.nonNull(addedFilm.getId()))
        );
    }

    @Test
    void shouldUpdateFilmByNewDataAndPreviousLongID() throws ExistException {
        FilmData newFilm = new FilmData(null, "aa", LocalDate.now(), "-", 10L);
        FilmData added = filmService.add(newFilm);
        FilmData updating = new FilmData(added.getId(), "updated", LocalDate.now(), "-", 10L);

        FilmData updated = filmService.update(updating);

        assertNotEquals(added.getName(), updated.getName(), "Name hasn't been updated");
        assertEquals(added.getId(), updated.getId(), "ID before and after update not equal");
        assertEquals("updated", updated.getName());
    }
}