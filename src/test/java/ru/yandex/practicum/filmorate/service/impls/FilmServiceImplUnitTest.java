package ru.yandex.practicum.filmorate.service.impls;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceImplUnitTest {

    private FilmService filmService;
    private Map<Long, Film> filmCollection;

    private Film film;

    @BeforeEach
    void setUp() {
        this.filmCollection = new HashMap<>();
        this.filmService = new FilmServiceImpl(filmCollection);
        this.film = Film.builder()
                .id(1L)
                .name("Name").description("-")
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
    }

    @Test
    void shouldAddNewFilmToCollection() {

        filmService.add(film);

        assertTrue(filmCollection.containsValue(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenAddNewFilmHasNotValidData() {
        int maxDescriptionLength = 200;
        String incorrectDescription = "a".repeat(maxDescriptionLength);
        LocalDate incorrectReleaseDate = LocalDate.of(1895, 12, 27);
        Class<ValidationException> clazz = ValidationException.class;

        assertAll("Should validate all Film Fields Requirements",
                () -> assertThrows(clazz, () -> filmService.add(film.toBuilder()
                        .name("").build()), "Empty name"),
                () -> assertThrows(clazz, () -> filmService.add(film.toBuilder()
                        .name(null).build()), "Null name"),
                () -> assertThrows(clazz, () -> filmService.add(film.toBuilder()
                        .description(incorrectDescription).build()), "Description length"),
                () -> assertThrows(clazz, () -> filmService.add(film.toBuilder()
                        .releaseDate(incorrectReleaseDate).build()), "Release date"),
                () -> assertThrows(clazz, () -> filmService.add(film.toBuilder()
                        .duration(0).build()), "Duration")
        );
    }

    @Test
    void shouldUpdateFilmInCollection() {
        filmCollection.put(film.getId(), film);

        filmService.update(film.toBuilder().name("Updated").build());

        assertEquals("Updated", filmCollection.get(film.getId()).getName());
    }

    @Test
    void shouldThrowNotFoundExceptionUpdatedFilmAbsentInCollection() {

        assertThrows(NotFoundException.class, () -> filmService.update(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdatedFilmHasNotValidData() {
        int maxDescriptionLength = 200;
        String incorrectDescription = "a".repeat(maxDescriptionLength);
        LocalDate incorrectReleaseDate = LocalDate.of(1895, 12, 27);
        Class<ValidationException> clazz = ValidationException.class;

        assertAll("Should validate all Film Fields Requirements",
                () -> assertThrows(clazz, () -> filmService.update(film.toBuilder()
                        .name("").build()), "Empty name"),
                () -> assertThrows(clazz, () -> filmService.update(film.toBuilder()
                        .name(null).build()), "Null name"),
                () -> assertThrows(clazz, () -> filmService.update(film.toBuilder()
                        .description(incorrectDescription).build()), "Description length"),
                () -> assertThrows(clazz, () -> filmService.update(film.toBuilder()
                        .releaseDate(incorrectReleaseDate).build()), "Release date"),
                () -> assertThrows(clazz, () -> filmService.update(film.toBuilder()
                        .duration(0).build()), "Duration")
        );
    }

    @Test
    void shouldReturnFilmCollection() {
        filmCollection.put(film.getId(),film);

        assertTrue(filmService.list().contains(film));
    }
}