package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidationGroup;

import java.util.List;

@Validated
public interface FilmStorage {

    @Validated(ValidationGroup.OnCreate.class)
    Film add(@Valid @NotNull Film film) throws ExistException;

    Film findById(long id) throws ExistException;

    @Validated(ValidationGroup.OnUpdate.class)
    Film update(@Valid @NotNull Film film) throws ExistException;

    List<Film> getAll();

    List<Film> sortByRateAndLimitResult(long listSize);
}