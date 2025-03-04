package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.dto.UserInfo;

import java.util.List;

public interface FriendService {

    void addToFriends(long userId, long friendId) throws ExistException;

    void removeFromFriends(long userId, long friendId) throws ExistException;

    List<UserInfo> listFriends(long userId) throws ExistException;

    List<UserInfo> listCommonFriends(long userId, long otherUserId) throws ExistException;
}