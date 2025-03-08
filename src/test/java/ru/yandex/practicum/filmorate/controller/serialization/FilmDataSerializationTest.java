package ru.yandex.practicum.filmorate.controller.serialization;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.yandex.practicum.filmorate.TestFilmData;
import ru.yandex.practicum.filmorate.model.dto.FilmData;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checks serialization for {@link FilmData}
 */

@JsonTest
public class FilmDataSerializationTest  {

    @Autowired
    private JacksonTester<FilmData> mapper;

    @Test
    void shouldSerializeFilmDataObjectToJson() throws IOException {

        FilmData film = TestFilmData.getFilmData();
        String json = TestFilmData.getFilmDataJson();

        assertThat(mapper.write(film)).isEqualToJson(json);
    }

    @Test
    void shouldDeserializeJsonToFilmDataObject() throws IOException {

        String json = TestFilmData.getFilmDataJson();
        FilmData film = TestFilmData.getFilmData();

        assertThat(mapper.parseObject(json)).isEqualTo(film);
    }
}