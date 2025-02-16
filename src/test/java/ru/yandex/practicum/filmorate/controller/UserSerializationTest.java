package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.yandex.practicum.filmorate.model.User;

/**
 * Test class for the {@link ObjectMapper}
 * Default JsonMapper from IOC Container
 * Checks requirement for {@link User}
 */

@JsonTest
public class UserSerializationTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldSerializeNameAsLoginWhenNameIsNull() throws JsonProcessingException {
        String name = null;

        User user = User.builder().name(name).login("de").id(1L).password("").build();
        String json = "{\"id\":1,\"login\":\"de\",\"email\":null,\"name\":\"de\",\"password\":\"\",\"birthday\":null}";

        Assertions.assertEquals(json, mapper.writeValueAsString(user));
    }

    @Test
    void shouldSerializeNameAsLoginWhenNameIsBlank() throws JsonProcessingException {
        String name = "";

        User user = User.builder().name(name).login("de").id(1L).password("").build();
        String json = "{\"id\":1,\"login\":\"de\",\"email\":null,\"name\":\"de\",\"password\":\"\",\"birthday\":null}";

        Assertions.assertEquals(json, mapper.writeValueAsString(user));
    }
}