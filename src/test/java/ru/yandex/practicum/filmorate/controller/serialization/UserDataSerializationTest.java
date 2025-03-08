package ru.yandex.practicum.filmorate.controller.serialization;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.yandex.practicum.filmorate.TestUserData;
import ru.yandex.practicum.filmorate.model.dto.UserData;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checks serialization for {@link UserData}
 */

@JsonTest
public class UserDataSerializationTest {

    @Autowired
    private JacksonTester<UserData> mapper;

    @Test
    void shouldSerializeUserDataObjectToJson() throws IOException {
        UserData user = TestUserData.getUserData();
        String json = TestUserData.getUserDataJson();

        assertThat(mapper.write(user)).isEqualToJson(json);
    }

    @Test
    void shouldDeserializeJsonToUserDataObject() throws IOException {
        String json = TestUserData.getUserDataJson();
        UserData user = TestUserData.getUserData();

        assertThat(mapper.parseObject(json)).isEqualTo(user);
    }
}