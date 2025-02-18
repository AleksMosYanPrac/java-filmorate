package ru.yandex.practicum.filmorate.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.filmorate.validation.ValidationGroup;

import java.time.LocalDate;

@Value
@AllArgsConstructor
@Builder
public class UserData {

    @Null(groups = ValidationGroup.OnCreate.class, message = "must be null")
    @NotNull(groups = ValidationGroup.OnUpdate.class, message = "must be provided")
    Long id;

    @NotEmpty
    @Pattern(regexp = "\\S+", message = "must not contain whitespaces")
    String login;

    @Email(message = "must be formatted as email")
    String email;

    String name;

    @Past(message = "must be a date in the past")
    LocalDate birthday;
}