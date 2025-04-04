package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.dto.FilmData;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.MPARating;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class TestFilmData {

    public static Film getFilm() {
        return Film.builder().id(1L).name("film").description("-").releaseDate(getDate()).duration(10)
                .genres(getGenreSet()).mpaRating(getMpaRating()).build();
    }

    public static Film getNewFilm() {
        return Film.builder().name("film ").description("-").releaseDate(getDate()).duration(10)
                .genres(getGenreSet()).mpaRating(getMpaRating()).build();
    }

    public static FilmData getFilmData() {
        return new FilmData(1L, "film", getDate(), "-", 10, getMpaRating(), getGenreSet());
    }

    public static FilmInfo getFilmInfo() {
        return new FilmInfo(1L, "film", getDate().format(DateTimeFormatter.ISO_DATE), "-", 10, 0,
                getGenreSet(), getMpaRating());
    }

    public static String getNewFilmJson() {
        return "{\"name\": \"film\",\"description\": \"-\",\"releaseDate\": \"2025-02-08\"," +
               "\"duration\": 10,\"mpa\": {\"id\": 1, \"name\": \"G\"}," +
               "\"genres\": [{\"id\": 1,\"name\": \"комедия\"}]}";
    }

    public static String getUpdatedFilmJson() {
        return "{" +
               "\"id\": 1,\"name\": \"updated\",\"description\": \"-\"," +
               "\"releaseDate\": \"2025-02-08\",\"duration\": 10," +
               "\"mpa\": {\"id\": 1, \"name\": \"G\"}," +
               "\"genres\": [{\"id\": 1,\"name\": \"комедия\"}]" +
               "}";
    }

    public static String getFilmDataJson() {
        return "{\n" +
               "  \"id\": 1,\n" +
               "  \"name\": \"film\",\n" +
               "  \"releaseDate\": \"2020-12-12\",\n" +
               "  \"description\": \"-\",\n" +
               "  \"duration\": 10,\n" +
               "  \"mpa\": {\"id\": 4, \"name\": \"R\"},\n" +
               "  \"genres\": [{\"id\": 6,\"name\": \"Боевик\"},{\"id\": 2,\"name\": \"Драма\"}]\n" +
               "}\n";
    }

    public static String getFilmInfoJson() {
        return "{\n" +
               "\"id\": 1,\n" +
               "\"name\": \"film\",\n" +
               "\"releaseDate\": \"2020-12-12\",\n" +
               "\"description\": \"-\",\n" +
               "\"duration\": 10,\n" +
               "\"rate\": 0,\n" +
               "\"genres\": [{\"id\": 6,\"name\": \"Боевик\"},{\"id\": 2,\"name\": \"Драма\"}],\n" +
               "\"mpa\": {\"id\": 4, \"name\": \"R\"}\n" +
               "}\n";
    }

    public static Set<Genre> getGenreSet() {
        return Set.of(new Genre(6L, "Боевик"), new Genre(2L, "Драма"));
    }

    public static MPARating getMpaRating() {
        return new MPARating(4L, "R");
    }

    private static LocalDate getDate() {
        return LocalDate.of(2020, 12, 12);
    }
}