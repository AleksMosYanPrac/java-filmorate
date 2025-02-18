package ru.yandex.practicum.filmorate.mapping.impls;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapping.FilmMapper;
import ru.yandex.practicum.filmorate.mapping.dto.FilmData;
import ru.yandex.practicum.filmorate.mapping.dto.FilmInfo;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class FilmMapperImpl implements FilmMapper {

    @Override
    public FilmData toFilmData(Film film) {
        return new FilmData(
                film.getId(),
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration()
        );
    }

    @Override
    public FilmInfo toFilmInfo(Film film) {
        return new FilmInfo(
                film.getId(),
                film.getName(),
                film.getFormattedReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.rate()
        );
    }

    @Override
    public Film toFilm(FilmData filmData) {
        return Film.builder()
                .id(filmData.getId())
                .name(filmData.getName())
                .releaseDate(filmData.getReleaseDate())
                .duration(filmData.getDuration())
                .description(filmData.getDescription())
                .build();
    }
}