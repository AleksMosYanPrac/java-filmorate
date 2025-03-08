package ru.yandex.practicum.filmorate.mapping;

import ru.yandex.practicum.filmorate.exception.ExistException;

public interface IdMapper {

    Long toUserId(long id) throws ExistException;

    Long toFilmId(long id) throws ExistException;
}