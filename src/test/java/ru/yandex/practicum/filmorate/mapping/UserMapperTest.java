package ru.yandex.practicum.filmorate.mapping;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.TestUserData;
import ru.yandex.practicum.filmorate.model.dto.UserData;
import ru.yandex.practicum.filmorate.model.dto.UserInfo;
import ru.yandex.practicum.filmorate.model.user.User;

/**
 * Test class for the {@link UserMapper}
 */

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private User user;
    private UserData userData;
    private UserInfo userInfo;

    @BeforeEach
    void init() {
        this.user = TestUserData.getUser();
        this.userData = TestUserData.getUserData();
        this.userInfo = TestUserData.getUserInfo();
    }

    @Test
    void shouldMapUserToUserData() {

        Assertions.assertEquals(userData, userMapper.toUserData(user));
    }

    @Test
    void shouldMapUserToUserInfo() {

        Assertions.assertEquals(userInfo, userMapper.toUserInfo(user));
    }

    @Test
    void shouldMapUserDataToUser() {

        Assertions.assertEquals(user, userMapper.toUser(userData));
    }
}