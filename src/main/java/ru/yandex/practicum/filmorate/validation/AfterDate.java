package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Documented
@Constraint(validatedBy = LocalDateIsAfterValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface AfterDate {

    String message() default "Date is not after {year}.{month}.{day}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int year();

    int month();

    int day();
}