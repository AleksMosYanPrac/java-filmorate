package ru.yandex.practicum.filmorate.controller.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;

/**
 * Checks serialization for {@link FilmInfo}
 */

public class FilmInfoSerializationTest extends AbstractSerializationTest {

    @Test
    void shouldSerializeFilmInfoObjectToJson() throws JsonProcessingException {
        FilmInfo film = new FilmInfo(1, "Name", "2000-12-12", "-", 10, 0);
        String json = "{\"id\":1,\"name\":\"Name\",\"releaseDate\":\"2000-12-12\"," +
                      "\"description\":\"-\",\"duration\":10,\"rate\":0}";


        Assertions.assertEquals(json, mapper.writeValueAsString(film));
    }
}