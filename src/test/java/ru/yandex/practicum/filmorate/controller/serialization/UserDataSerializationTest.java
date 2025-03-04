package ru.yandex.practicum.filmorate.controller.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.dto.UserData;

import java.time.LocalDate;

/**
 * Checks serialization for {@link UserData}
 */

public class UserDataSerializationTest extends AbstractSerializationTest {

    @Test
    void shouldSerializeUserDataObjectToJson() throws JsonProcessingException {
        UserData user = UserData.builder().name("name").login("de").id(1L).build();
        String json = "{\"id\":1,\"login\":\"de\",\"email\":null,\"name\":\"name\",\"birthday\":null}";

        Assertions.assertEquals(json, mapper.writeValueAsString(user));
    }

    @Test
    void shouldDeserializeJsonToUserDataObject() throws JsonProcessingException {
        LocalDate birthday = LocalDate.of(2000, 12, 12);
        UserData user = UserData.builder().name("name").login("de").id(1L).birthday(birthday).build();
        String json = "{\"id\":1,\"login\":\"de\",\"email\":null,\"name\":\"name\",\"birthday\":\"2000-12-12\"}";

        Assertions.assertEquals(user, mapper.readValue(json, UserData.class));
    }
}