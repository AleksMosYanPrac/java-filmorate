package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationGroup;

import java.util.List;

@Validated
public interface UserStorage {

    @Validated(ValidationGroup.OnCreate.class)
    User add(@Valid @NotNull User user) throws ExistException;

    User findById(long id) throws ExistException;

    @Validated(ValidationGroup.OnUpdate.class)
    User update(@Valid @NotNull User user) throws ExistException;

    List<User> getAll();

    List<User> findFriendsForUserById(long userId) throws ExistException;

    List<User> findCommonFriendsForUsersById(long userId, long otherUserId) throws ExistException;
}