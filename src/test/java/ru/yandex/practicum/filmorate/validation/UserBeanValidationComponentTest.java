package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/*
 * reference to Java Bean Validation provided by Spring Framework
 * https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html#page-title
 *
 * reference to Jakarta Bean Validation provided by JakartaEE
 * https://jakarta.ee/specifications/bean-validation/3.0/
 */

/**
 * Test class for the {@link Validator} <br>
 * Checks Bean Validation for {@link User} <br>
 */

@SpringBootTest
public class UserBeanValidationComponentTest {

    @Autowired
    private Validator validator;

    @Test
    void shouldValidateWhenUserIsValid() {
        User user = User.builder().login("ex").email("ex@ex.com").birthday(LocalDate.of(2000, 12, 1)).build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldValidateWithGroupWhenUserIsValid() {
        User user = User.builder().id(1L).login("ex").email("ex@ex.com").birthday(LocalDate.of(2000, 12, 1)).build();

        Set<ConstraintViolation<User>> violations = validator.validate(user, ValidationGroup.OnUpdate.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotValidateWithGroupWhenUserIsIsValidButAnotherFieldsNot() {
        User user = User.builder().id(1L).login(" ex ").email("ex@ex.com").birthday(LocalDate.of(2000, 12, 1)).build();

        Set<ConstraintViolation<User>> violations = validator.validate(user, ValidationGroup.OnUpdate.class);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not contain whitespaces");
    }

    @Test
    void shouldNotValidateWhenLoginHasWhitespaces() {
        User user = User.builder().login(" ex ").email("ex@ex.com").birthday(LocalDate.of(2000, 12, 1)).build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not contain whitespaces");
    }

    @Test
    void shouldNotValidateWhenEmailNotFormatted() {
        User user = User.builder().login("ex").email("exes.com").birthday(LocalDate.of(2000, 12, 1)).build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must be formatted as email");
    }

    @Test
    void shouldNotValidateWhenBirthdayToday() {
        User user = User.builder().login("ex").email("ex@ex.com").birthday(LocalDate.now()).build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must be a date in the past");
    }
}