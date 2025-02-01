package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationGroup;

import java.util.List;

@Validated
public interface UserService {

    User create(@Valid @NotNull User user) throws ConstraintViolationException;

    @Validated(ValidationGroup.OnUpdate.class)
    User update(@Valid @NotNull User user) throws ConstraintViolationException;

    List<User> list();
}