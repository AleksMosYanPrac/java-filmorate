package ru.yandex.practicum.filmorate.controller.serialization;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.yandex.practicum.filmorate.TestUserData;
import ru.yandex.practicum.filmorate.model.dto.UserInfo;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checks serialization for {@link UserInfo}
 */

@JsonTest
public class UserInfoSerializationTest {

    @Autowired
    private JacksonTester<UserInfo> mapper;

    @Test
    void shouldSerializeUserInfoObjectToJson() throws IOException {
        UserInfo user = TestUserData.getUserInfo();
        String json = TestUserData.getUserInfoJson();

        assertThat(mapper.write(user)).isEqualToJson(json);
    }
}