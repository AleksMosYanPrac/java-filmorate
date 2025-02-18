package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.dto.UserData;
import ru.yandex.practicum.filmorate.model.dto.UserInfo;

import java.util.List;

public interface UserService {

    UserData create(UserData user) throws ExistException;

    UserData update(UserData user) throws ExistException;

    List<UserData> list();

    UserInfo getById(long id) throws ExistException;
}