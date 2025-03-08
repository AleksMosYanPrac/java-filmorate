package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.user.Friends;
import ru.yandex.practicum.filmorate.repository.jdbc.FriendsStorageImpl;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(FriendsStorageImpl.class)
class FriendsStorageTest {

    @Autowired
    private FriendsStorage friendsStorage;

    @Test
    @Sql("/db/h2/tests/friends-test-data.sql")
    void shouldFindFriendsEntityByProvidedUserId() {
        assertThat(friendsStorage.findById(1L))
                .isNotNull()
                .hasFieldOrPropertyWithValue("userId", 1L)
                .satisfies((d) -> assertThat(d.getFriendsId()).containsExactly(2L));
    }

    @Test
    @Sql("/db/h2/tests/user-test-data.sql")
    void shouldFindFriendsEntityByProvidedUserIdWhenNoRecordsInTable() {
        assertThat(friendsStorage.findById(1L))
                .isNotNull()
                .hasFieldOrPropertyWithValue("userId", 1L)
                .satisfies((d) -> assertThat(d.getFriendsId()).isEmpty());
    }

    @Test
    @Sql("/db/h2/tests/user-test-data.sql")
    void shouldSaveNewFriendsEntity() {
        Friends friends = new Friends(1L,new HashMap<>());
        friends.add(2L);
        friends.add(3L);
        friendsStorage.save(friends);

        assertThat(friendsStorage.findById(1L).getFriendsId()).containsExactly(2L, 3L);
    }

    @Test
    @Sql("/db/h2/tests/friends-test-data.sql")
    void shouldSaveUpdatedFriendsEntity() {
        Friends friends = friendsStorage.findById(1L);
        friends.remove(2L);
        friendsStorage.save(friends);

        assertThat(friendsStorage.findById(1L).getFriendsId()).isEmpty();
    }
}