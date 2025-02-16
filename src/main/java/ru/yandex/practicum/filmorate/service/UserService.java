package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static ru.yandex.practicum.filmorate.validation.ValidationGroup.*;

@Validated
public interface UserService {

    @Validated(OnCreate.class)
    User create(@Valid @NotNull User user) throws ExistException;

    @Validated(OnUpdate.class)
    User update(@Valid @NotNull User user) throws ExistException;

    List<User> list();
}