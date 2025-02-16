package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class LocalDateIsAfterValidator implements ConstraintValidator<AfterDate, LocalDate> {

    private LocalDate dateInPast;

    @Override
    public void initialize(AfterDate constraint) {
        this.dateInPast = LocalDate.of(constraint.year(), constraint.month(), constraint.day());
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date.isAfter(dateInPast);
    }
}
