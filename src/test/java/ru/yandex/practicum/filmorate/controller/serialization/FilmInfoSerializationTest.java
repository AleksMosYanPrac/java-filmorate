package ru.yandex.practicum.filmorate.controller.serialization;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.yandex.practicum.filmorate.TestFilmData;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checks serialization for {@link FilmInfo}
 */

@JsonTest
public class FilmInfoSerializationTest {

    @Autowired
    private JacksonTester<FilmInfo> mapper;

    @Test
    void shouldSerializeFilmInfoObjectToJson() throws IOException {

        FilmInfo film = TestFilmData.getFilmInfo();
        String json = TestFilmData.getFilmInfoJson();

        assertThat(mapper.write(film)).isEqualToJson(json);
    }
}