package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendService {

    void addToFriends(long userId, long friendId) throws ExistException;

    void removeFromFriends(long userId, long friendId) throws ExistException;

    List<User> listFriends(long userId) throws ExistException;

    List<User> listCommonFriends(long userId, long otherUserId) throws ExistException;
}