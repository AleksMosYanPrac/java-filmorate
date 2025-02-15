package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

import static ru.yandex.practicum.filmorate.validation.ValidationGroup.*;

@Validated
public interface FilmService {

    @Validated(OnCreate.class)
    Film add(@Valid @NotNull Film film) throws ExistException;

    @Validated(OnUpdate.class)
    Film update(@Valid @NotNull Film film) throws ExistException;

    List<Film> list();
}