package ru.yandex.practicum.filmorate.service.impls;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.Film;
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

    @Nested
    class AddingNewFilm {

        @Test
        void onAddShouldThrowValidateExceptionWhenFilmContainsNotValidData() {
            Film newFilm = Film.builder().name("").duration(100L).releaseDate(LocalDate.now()).build();

            assertThrows(ConstraintViolationException.class, () -> filmService.add(newFilm));
        }

        @Test
        void onAddShouldThrowValidateExceptionWhenFilmIsNull() {
            Film newFilm = null;

            assertThrows(ConstraintViolationException.class, () -> filmService.add(newFilm));
        }

        @Test
        void onAddShouldThrowValidateExceptionWhenAddedFilmHasLongID() {
            Film newFilm = Film.builder().id(1L).name("asd").duration(100L).releaseDate(LocalDate.now()).build();

            assertThrows(ConstraintViolationException.class, () -> filmService.add(newFilm));
        }

        @Test
        void shouldAddFilmBySettingLongID() throws ExistException {
            Film newFilm = Film.builder().name("ap").duration(100L).releaseDate(LocalDate.now()).build();

            Film addedFilm = filmService.add(newFilm);

            assertAll("Check Long id before and after add",
                    () -> assertTrue(Objects.isNull(newFilm.getId())),
                    () -> assertTrue(Objects.nonNull(addedFilm.getId()))
            );
        }

        @Test
        void shouldAddThanThrowExistExceptionWhenAddedFilmAlreadyAddedWithProvidedNameAndReleaseDate()
                throws ExistException {
            Film newFilm = Film.builder().name("asd").duration(100L).releaseDate(LocalDate.now()).build();
            filmService.add(newFilm);

            assertThrows(ExistException.class, () -> filmService.add(newFilm));
        }
    }

    @Nested
    class UpdatingFilm {

        @Test
        void onUpdateShouldThrowValidateExceptionWhenFilmNotContainLongID() {
            Film updating = Film.builder().name("a").duration(100L).releaseDate(LocalDate.now()).build();

            assertThrows(ConstraintViolationException.class, () -> filmService.update(updating));
        }

        @Test
        void onUpdateShouldThrowValidateExceptionWhenFilmIsNull() {
            Film updating = null;

            assertThrows(ConstraintViolationException.class, () -> filmService.update(updating));
        }

        @Test
        void onUpdateShouldThrowValidateExceptionWhenFilmContainNotValidData() {
            Film updating = Film.builder().id(1L).name(" ").duration(100L).releaseDate(LocalDate.now()).build();

            assertThrows(ConstraintViolationException.class, () -> filmService.update(updating));
        }

        @Test
        void shouldUpdateFilmByNewDataAndPreviousLongID() throws ExistException {
            Film newFilm = Film.builder().name("asd").duration(100L).releaseDate(LocalDate.now()).build();
            Film added = filmService.add(newFilm);
            Film updating = added.toBuilder().name("updated").build();

            Film updated = filmService.update(updating);

            assertNotEquals(added.getName(), updated.getName(), "Name hasn't been updated");
            assertEquals(added.getId(), updated.getId(), "ID before and after update not equal");
            assertEquals("updated", updated.getName());
        }

        @Test
        void shouldUpdateThanThrowExistExceptionWhenUpdatingFilmHasNotAddedWithProvidedLongId() {
            Film updating = Film.builder().id(Long.MAX_VALUE).name("a").duration(100L)
                    .releaseDate(LocalDate.now()).build();

            assertThrows(ExistException.class, () -> filmService.update(updating));
        }
    }
}