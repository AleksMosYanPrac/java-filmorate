package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.mapping.FilmMapper;
import ru.yandex.practicum.filmorate.model.dto.FilmData;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final FilmMapper filmMapper;

    @Override
    public FilmData add(FilmData film) throws ExistException {
        return filmMapper.toFilmData(filmStorage.add(filmMapper.toFilm(film)));
    }

    @Override
    public FilmData update(FilmData film) throws ExistException {
        Film updatingFilm = filmStorage.findById(film.getId())
                .toBuilder()
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();

        return filmMapper.toFilmData(filmStorage.update(updatingFilm));
    }

    @Override
    public List<FilmData> list() {
        return filmStorage.getAll().stream().map(filmMapper::toFilmData).toList();
    }

    @Override
    public FilmInfo getById(long id) throws ExistException {
        return filmMapper.toFilmInfo(filmStorage.findById(id));
    }
}