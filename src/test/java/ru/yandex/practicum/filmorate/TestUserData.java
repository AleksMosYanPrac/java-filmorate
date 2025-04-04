package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.dto.UserData;
import ru.yandex.practicum.filmorate.model.dto.UserInfo;
import ru.yandex.practicum.filmorate.model.user.Friends;
import ru.yandex.practicum.filmorate.model.user.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class TestUserData {

    public static User getUser() {
        return User.builder().id(1L).login("login").name("name").email("a@a.com").birthday(LocalDate.of(2000, 10, 10))
                .friends(getFriends())
                .build();
    }

    public static User getNewUser() {
        return User.builder().login("login").name("name").email("a@a.com").birthday(LocalDate.of(2000, 10, 10))
                .friends(getFriends())
                .build();
    }

    public static User getUpdatedUser() {
        return getUser().toBuilder().login("updated").build();
    }

    public static UserData getUserData() {
        return new UserData(1L, "login", "a@a.com", "name", LocalDate.of(2000, 10, 10));
    }

    public static String getUserDataJson() {
        return "{\n" +
               "\"id\": 1,\n" +
               "\"login\": \"login\",\n" +
               "\"email\": \"a@a.com\",\n" +
               "\"name\": \"name\",\n" +
               "\"birthday\": \"2000-10-10\"\n" +
               "}\n";
    }

    public static UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setName("name");
        userInfo.setFriends(Set.of(2L, 3L));
        return userInfo;
    }

    public static String getUserInfoJson() {
        return "{\n" +
               "\"id\": 1,\n" +
               "\"name\": \"name\",\n" +
               "\"user_friends\": [{\"id\": 2},{\"id\": 3}]\n" +
               "}\n";
    }

    private static Friends getFriends() {
        return new Friends(1L, Map.of(2L, FriendshipStatus.REQUESTED, 3L, FriendshipStatus.REQUESTED));
    }

    public static String getNewUserDataJson() {
        return "{\n" +
               "\"login\": \"login\",\n" +
               "\"email\": \"a@a.com\",\n" +
               "\"name\": \"name\",\n" +
               "\"birthday\": \"2000-10-10\"\n" +
               "}\n";
    }
}