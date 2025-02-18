package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.dto.FilmData;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;

import java.util.List;


public interface FilmService {

    FilmData add(FilmData film) throws ExistException;

    FilmData update(FilmData film) throws ExistException;

    List<FilmData> list();

    FilmInfo getById(long id) throws ExistException;
}