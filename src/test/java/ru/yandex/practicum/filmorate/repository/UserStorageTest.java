package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.TestUserData;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.repository.jdbc.UserStorageImpl;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(UserStorageImpl.class)
class UserStorageTest {

    @Autowired
    private UserStorage userStorage;

    @Test
    void shouldCreateRecordInUsersTableAndReturnGeneratedId() throws ExistException {
        User user = userStorage.create(TestUserData.getNewUser());
        assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Sql("/db/h2/tests/user-test-data.sql")
    void shouldFindUserEntityById() throws ExistException {
        assertThat(userStorage.findById(1L).get()).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Sql("/db/h2/tests/user-test-data.sql")
    void shouldUpdateRecordInUsersTable() throws ExistException {
        assertThat(userStorage.update(TestUserData.getUpdatedUser()))
                .hasFieldOrPropertyWithValue("login", "updated");
    }

    @Test
    @Sql("/db/h2/tests/user-test-data.sql")
    void shouldFindAllUserEntities() {
        assertThat(userStorage.findAll()).isNotEmpty();
    }

    @Test
    @Sql("/db/h2/tests/friends-test-data.sql")
    void shouldFindUserEntitiesWhichIsFriendsForProvidedByIdUser() throws ExistException {
        assertThat(userStorage.findFriendsForUserById(1L))
                .extracting(User::getId)
                .containsSequence(2L);
    }

    @Test
    @Sql("/db/h2/tests/friends-test-data.sql")
    void shouldFindUserEntitiesWhichIsCommonFriendsForProvidedUsersById() throws ExistException {
        assertThat(userStorage.findCommonFriendsForUsersById(1L, 3L))
                .extracting(User::getId)
                .containsSequence(2L);
    }
}