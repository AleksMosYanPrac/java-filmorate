package ru.yandex.practicum.filmorate.service.impls;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.dto.UserInfo;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Test class for the {@link FriendService}
 */

@SpringBootTest
public class FriendServiceTest {

    @Autowired
    private FriendService friendService;

    @MockBean
    private UserStorage userStorage;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void init() throws ExistException {
        this.user1 = User.builder().id(1L).login("1").email("e@x.com").birthday(LocalDate.now()).build();
        this.user2 = User.builder().id(2L).login("2").email("ee@x.com").birthday(LocalDate.now()).build();
        this.user3 = User.builder().id(3L).login("3").email("eee@x.com").birthday(LocalDate.now()).build();

        when(userStorage.findById(user1.getId())).thenReturn(user1);
        when(userStorage.findById(user2.getId())).thenReturn(user2);
        when(userStorage.findById(user3.getId())).thenReturn(user3);
        when(userStorage.update(any())).thenReturn(any());
    }

    @Test
    void shouldAddToFriends() throws ExistException {

        friendService.addToFriends(user1.getId(), user2.getId());

        assertThat(user1.getFriendsId()).contains(user2.getId());
        assertThat(user2.getFriendsId()).contains(user1.getId());
    }

    @Test
    void shouldRemoveFromFriends() throws ExistException {
        friendService.addToFriends(user1.getId(), user2.getId());

        friendService.removeFromFriends(user1.getId(), user2.getId());

        assertThat(user1.getFriendsId()).doesNotContain(user2.getId());
        assertThat(user2.getFriendsId()).doesNotContain(user1.getId());
    }

    @Test
    void shouldTakeListFriends() throws ExistException {
        when(userStorage.findFriendsForUserById(user1.getId())).thenReturn(List.of(user2, user3));

        assertThat(friendService.listFriends(user1.getId()))
                .map(UserInfo::getId)
                .contains(user2.getId(), user3.getId());
    }

    @Test
    void shouldTakeListCommonFriends() throws ExistException {
        friendService.addToFriends(user1.getId(), user2.getId());
        friendService.addToFriends(user1.getId(), user3.getId());
        when(userStorage.findCommonFriendsForUsersById(user1.getId(),user3.getId())).thenReturn(List.of(user2));

        assertThat(friendService.listCommonFriends(user1.getId(), user3.getId()))
                .map(UserInfo::getId).contains(user2.getId());
    }
}