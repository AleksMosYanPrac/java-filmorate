package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.user.Friends;

public interface FriendsStorage {

    Friends findById(Long userId);

    Friends save(Friends friends);
}