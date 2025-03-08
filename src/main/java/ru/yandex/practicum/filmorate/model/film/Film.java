package ru.yandex.practicum.filmorate.model.film;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.filmorate.validation.AfterDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static ru.yandex.practicum.filmorate.validation.ValidationGroup.*;

/**
 * Требования к полям класса:
 * <p> 1. название не может быть пустым;
 * <p> 2. максимальная длина описания — 200 символов;
 * <p> 3. дата релиза — не раньше 28 декабря 1895 года;
 * <p> 4. продолжительность фильма должна быть положительным числом.
 */
@Value
@Builder(toBuilder = true)
public class Film {

    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE;

    @Null(groups = OnCreate.class, message = "must be null")
    @NotNull(groups = OnUpdate.class, message = "must be not null")
    Long id;

    @NotBlank(message = "must be not blank")
    String name;

    @AfterDate(year = 1895, month = 12, day = 28)
    LocalDate releaseDate;

    @Size(max = 200, message = "must be less then {max} symbols")
    String description;

    @Positive
    long duration;

    Set<Genre> genres;

    MPARating mpaRating;

    @Builder.Default
    LikesRating likesRating = new LikesRating(null,new HashSet<>());

    public long rate() {
        return likesRating.getRating();
    }

    public String getFormattedReleaseDate() {
        return releaseDate.format(FORMATTER);
    }

}