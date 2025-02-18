package ru.yandex.practicum.filmorate.controller.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.mapping.dto.FilmData;

import java.time.LocalDate;

/**
 * Checks serialization for {@link FilmData}
 */

public class FilmDataSerializationTest extends AbstractSerializationTest {

    @Test
    void shouldSerializeFilmDataObjectToJson() throws JsonProcessingException {
        FilmData film = new FilmData(1L, "de", null, null, 10);
        String json = "{\"id\":1,\"name\":\"de\",\"releaseDate\":null,\"description\":null,\"duration\":10}";

        Assertions.assertEquals(json, mapper.writeValueAsString(film));
    }

    @Test
    void shouldDeserializeJsonToFilmDataObject() throws JsonProcessingException {
        LocalDate releaseDate = LocalDate.of(2000, 12, 12);
        String json = "{\"id\":1,\"name\":\"de\",\"description\":null,\"releaseDate\":\"2000-12-12\",\"duration\":10}";
        FilmData film = new FilmData(1L, "de", releaseDate, null, 10);

        Assertions.assertEquals(film, mapper.readValue(json, FilmData.class));
    }
}