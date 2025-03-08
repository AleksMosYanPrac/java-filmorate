package ru.yandex.practicum.filmorate.mapping.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.mapping.FilmMapper;
import ru.yandex.practicum.filmorate.model.dto.FilmData;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MPARatingService;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class FilmMapperImpl implements FilmMapper {

    private final GenreService genreService;
    private final MPARatingService mpaRatingService;

    @Override
    public Film toFilm(FilmData filmData) throws ExistException {
        return Film.builder()
                .id(filmData.getId())
                .name(filmData.getName())
                .releaseDate(filmData.getReleaseDate())
                .duration(filmData.getDuration())
                .description(filmData.getDescription())
                .mpaRating(mpaRatingService.getMPARatingById(filmData.getMpa().getId()))
                .genres(genreService.getGenresByIdList(
                        filmData.getGenres().stream()
                                .map(Genre::getId)
                                .collect(Collectors.toSet())))
                .build();
    }

    @Override
    public FilmData toFilmData(Film film) {
        return new FilmData(
                film.getId(),
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getMpaRating(),
                film.getGenres()
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
                film.rate(),
                film.getGenres(),
                film.getMpaRating()
        );
    }
}