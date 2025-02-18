package ru.yandex.practicum.filmorate.controller.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.mapping.dto.UserInfo;

import java.util.Set;

/**
 * Checks serialization for {@link UserInfo}
 */

public class UserInfoSerializationTest extends AbstractSerializationTest {

    @Test
    void shouldSerializeUserInfoObjectToJson() throws JsonProcessingException {
        UserInfo user = new UserInfo();
        user.setId(1);
        user.setName("name");
        user.setFriends(Set.of(2L, 3L));
        String json = "{\"id\":1,\"name\":\"name\",\"user_friends\":[{\"id\":2},{\"id\":3}]}";

        Assertions.assertEquals(json, mapper.writeValueAsString(user));
    }
}