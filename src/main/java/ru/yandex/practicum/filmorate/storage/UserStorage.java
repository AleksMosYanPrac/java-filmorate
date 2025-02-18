package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface UserStorage {
    User add(User user);

    Optional<User> findById(long id);

    User update(User user);

    List<User> getAll();

    boolean contains(Predicate<User> predicate);
}