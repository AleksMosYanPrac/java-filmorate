package ru.yandex.practicum.filmorate.service.impls;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.dto.UserData;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link UserService}
 */

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateUserBySettingLongID() throws ExistException {
        UserData newUser = UserData.builder().login("as").email("x@x.com").birthday(LocalDate.of(2000, 12, 1)).build();

        UserData created = userService.create(newUser);

        assertAll("Check Long id before and after create",
                () -> assertTrue(Objects.isNull(newUser.getId())),
                () -> assertTrue(Objects.nonNull(created.getId()))
        );
    }

    @Test
    void shouldUpdateUserByNewDataAndPreviousLongID() throws ExistException {
        UserData newUser = UserData.builder().login("asa").email("xx@x.com").birthday(LocalDate.of(2000, 12, 1)).build();
        UserData created = userService.create(newUser);
        UserData updatingUser = new UserData(created.getId(), "updated",
                created.getEmail(), created.getName(), created.getBirthday());

        UserData updated = userService.update(updatingUser);

        assertNotEquals(created.getLogin(), updated.getLogin(), "Login hasn't been updated");
        assertEquals(created.getId(), updated.getId(), "ID before and after update not equal");
        assertEquals("updated", updated.getLogin());
    }
}