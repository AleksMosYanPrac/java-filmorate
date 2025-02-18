package ru.yandex.practicum.filmorate.mapping;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.dto.UserData;
import ru.yandex.practicum.filmorate.model.dto.UserInfo;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

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
        LocalDate date = LocalDate.of(2000, 12, 12);
        this.user = User.builder().id(1L).login("login").email("email").name("test").birthday(date)
                .friendsId(Set.of(2L)).build();
        this.userData = UserData.builder().id(1L).login("login").email("email").name("test").birthday(date).build();
        this.userInfo = createUserInfo();
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
        User expectedUser = User.builder().id(1L).login("login").email("email").name("test")
                .birthday(LocalDate.of(2000, 12, 12)).build();

        Assertions.assertEquals(expectedUser, userMapper.toUser(userData));
    }

    private UserInfo createUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setName("test");
        userInfo.setFriends(Set.of(2L));
        return userInfo;
    }
}