package ru.yandex.practicum.filmorate.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.validation.ValidationGroup;

import java.util.List;
import java.util.Optional;

@Validated
public interface FilmStorage {

    @Validated(ValidationGroup.OnCreate.class)
    Film create(@Valid @NotNull Film film);

    Optional<Film> findById(long id);

    @Validated(ValidationGroup.OnUpdate.class)
    Film update(@Valid @NotNull Film film);

    List<Film> findAll();

    List<Film> sortByRateAndLimitResult(long listSize);
}