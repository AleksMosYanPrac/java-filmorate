package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.AfterDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ru.yandex.practicum.filmorate.validation.ValidationGroup.*;

/**
 * Требования к полям класса:
 * <p> 1. название не может быть пустым;
 * <p> 2. максимальная длина описания — 200 символов;
 * <p> 3. дата релиза — не раньше 28 декабря 1895 года;
 * <p> 4. продолжительность фильма должна быть положительным числом.
 */
@Data
@Builder(toBuilder = true)
public class Film {

    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE;

    @Null(groups = OnCreate.class, message = "must be null")
    @NotNull(groups = OnUpdate.class, message = "must be not null")
    private Long id;

    @NotBlank(message = "must be not blank")
    private String name;

    @AfterDate(year = 1895, month = 12, day = 28)
    private LocalDate releaseDate;

    @Size(max = 200, message = "must be less then {max} symbols")
    private String description;

    @Positive
    private long duration;

    public String getFormattedReleaseDate() {
        return releaseDate.format(FORMATTER);
    }
}