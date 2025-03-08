package ru.yandex.practicum.filmorate.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.validation.ValidationGroup;

import java.util.List;
import java.util.Optional;

@Validated
public interface UserStorage {

    @Validated(ValidationGroup.OnCreate.class)
    User create(@Valid @NotNull User user);

    Optional<User> findById(long id);

    @Validated(ValidationGroup.OnUpdate.class)
    User update(@Valid @NotNull User user);

    List<User> findAll();

    List<User> findFriendsForUserById(long userId);

    List<User> findCommonFriendsForUsersById(long userId, long otherUserId);
}