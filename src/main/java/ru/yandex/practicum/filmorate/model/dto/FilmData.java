package ru.yandex.practicum.filmorate.model.dto;

import jakarta.validation.constraints.*;
import lombok.Value;
import ru.yandex.practicum.filmorate.validation.AfterDate;
import ru.yandex.practicum.filmorate.validation.ValidationGroup;

import java.time.LocalDate;

@Value
public class FilmData {

    @Null(groups = ValidationGroup.OnCreate.class, message = "must be null")
    @NotNull(groups = ValidationGroup.OnUpdate.class, message = "must be not null")
    Long id;

    @NotBlank(message = "must be not blank")
    String name;

    @AfterDate(year = 1895, month = 12, day = 28)
    LocalDate releaseDate;

    @Size(max = 200, message = "must be less then {max} symbols")
    String description;

    @Positive
    long duration;
}