package ru.yandex.practicum.filmorate.validation;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * reference to Java Bean Validation provided by Spring Framework
 * https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html#page-title
 *
 * reference to Jakarta Bean Validation provided by JakartaEE
 * https://jakarta.ee/specifications/bean-validation/3.0/
 */

/**
 * Test class for the {@link Validator} <br>
 * Checks Bean Validation for {@link Film} <br>
 */

@SpringBootTest
public class FilmBeanValidationTest {

    @Autowired
    private Validator validator;

    @Test
    void shouldValidateWhenFilmIsValid() {
        Film film = Film.builder().name("Name").duration(100L).releaseDate(LocalDate.now()).description("-").build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldValidateWithOnCreateGroupWhenFilmIsValid() {
        Film film = Film.builder().name("Name").duration(100L).releaseDate(LocalDate.now()).description("-").build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film, ValidationGroup.OnCreate.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotValidateWithOnCreateGroupWhenDefaultGroupIsNotValid() {
        Film film = Film.builder().name(" ").duration(100L).releaseDate(LocalDate.now()).description("-").build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film, ValidationGroup.OnCreate.class);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must be not blank");
    }

    @Test
    void shouldValidateWithOnUpdateGroupWhenFilmIsValid() {
        Film film = Film.builder().id(1L).name("Name").duration(100L).releaseDate(LocalDate.now())
                .description("-").build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film, ValidationGroup.OnUpdate.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotValidateWithOnUpdateGroupWhenDefaultGroupIsNotValid() {
        Film film = Film.builder().id(1L).name("").duration(100L).releaseDate(LocalDate.now())
                .description("-").build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film, ValidationGroup.OnUpdate.class);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must be not blank");
    }

    @Test
    void shouldNotValidateWhenFilmNameIsBlank() {
        Film film = Film.builder().name("").duration(1L).releaseDate(LocalDate.of(1895, 12, 29))
                .description("-").build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must be not blank");
    }

    @Test
    void shouldNotValidateWhenFilmReleaseDateBeforeRequiredDate() {
        Film film = Film.builder().name("film").duration(1L).releaseDate(LocalDate.of(1895, 12, 27))
                .description("-").build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(ConstraintViolation::getPropertyPath)
                .extracting(Path::toString)
                .contains("releaseDate");
    }

    @Test
    void shouldNotValidateWhenFilmDescriptionLengthMoreThanRequired() {
        StringBuilder sb = new StringBuilder();
        Film film = Film.builder().name("film").duration(1L).releaseDate(LocalDate.of(1895, 12, 29))
                .description(sb.repeat("q", 201).toString()).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getPropertyPath)
                .extracting(Path::toString)
                .contains("description");
    }

    @Test
    void shouldNotValidateWhenFilmDurationNotPositive() {
        Film film = Film.builder().name("film").duration(0L).releaseDate(LocalDate.of(1895, 12, 29))
                .description("-").build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getPropertyPath)
                .extracting(Path::toString)
                .contains("duration");
    }
}