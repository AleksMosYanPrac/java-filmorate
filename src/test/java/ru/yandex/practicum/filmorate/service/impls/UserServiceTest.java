package ru.yandex.practicum.filmorate.service.impls;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.User;
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
    void onCreateShouldThrowValidateExceptionWhenUserContainsNotValidData() {
        User creatingUser = User.builder().login("asd").email("ex").birthday(LocalDate.now()).build();

        assertThrows(ConstraintViolationException.class, () -> userService.create(creatingUser));
    }

    @Test
    void onCreateShouldThrowValidateExceptionWhenUserIsNull() {
        User creatingUser = null;

        assertThrows(ConstraintViolationException.class, () -> userService.create(creatingUser));
    }

    @Test
    void onCreateShouldThrowValidateExceptionWhenCreatingUserHasBeenAddedWithProvidedLongID() {
        User newUser = User.builder().id(1L).login("asd").email("ex@ex.com").birthday(LocalDate.of(2000, 12, 1)).build();

        assertThrows(ConstraintViolationException.class, () -> userService.create(newUser));
    }

    @Test
    void shouldCreateUserBySettingLongID() throws ExistException {
        User creatingUser = User.builder().login("as").email("x@x.com").birthday(LocalDate.of(2000, 12, 1)).build();

        User created = userService.create(creatingUser);

        assertAll("Check Long id before and after create",
                () -> assertTrue(Objects.isNull(creatingUser.getId())),
                () -> assertTrue(Objects.nonNull(created.getId()))
        );
    }

    @Test
    void shouldCreateThanThrowExistExceptionWhenCreatingUserAlreadyAddedWithProvidedLoginAndEmail()
            throws ExistException {
        User creatingUser = User.builder().login("a").email("ex@ex.com").birthday(LocalDate.of(2000, 12, 1)).build();
        userService.create(creatingUser);

        assertThrows(ExistException.class, () -> userService.create(creatingUser));
    }

    @Test
    void onUpdateShouldThrowValidateExceptionWhenUserNotContainLongID() {
        User updatingUser = User.builder().login("a").email("ex@ex.com").birthday(LocalDate.of(2000, 12, 1)).build();

        assertThrows(ConstraintViolationException.class, () -> userService.update(updatingUser));
    }

    @Test
    void onUpdateShouldThrowValidateExceptionWhenUserContainIdAndNotValidData() {
        User updatingUser = User.builder().id(1L).login(" ").email("ex@ex.com").birthday(LocalDate.now()).build();

        assertThrows(ConstraintViolationException.class, () -> userService.update(updatingUser));
    }

    @Test
    void onUpdateShouldThrowValidateExceptionWhenUserIsNull() {
        User updatingUser = null;

        assertThrows(ConstraintViolationException.class, () -> userService.create(updatingUser));
    }

    @Test
    void shouldUpdateUserByNewDataAndPreviousLongID() throws ExistException {
        User creatingUser = User.builder().login("aaa").email("e@e.com").birthday(LocalDate.of(2000, 12, 1)).build();
        User created = userService.create(creatingUser);
        User updatingUser = created.toBuilder().login("updated").build();

        User updated = userService.update(updatingUser);

        assertNotEquals(created.getLogin(), updated.getLogin(), "Login hasn't been updated");
        assertEquals(created.getId(), updated.getId(), "ID before and after update not equal");
        assertEquals("updated", updated.getLogin());
    }

    @Test
    void shouldUpdateThanThrowExistExceptionWhenUpdatingUserHasNotAddedWithProvidedLongID() {
        User updating = User.builder().id(Long.MAX_VALUE)
                .login("asd").email("ex@ex.com").birthday(LocalDate.of(2000, 12, 1)).build();

        assertThrows(ExistException.class, () -> userService.update(updating));
    }
}