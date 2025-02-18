package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static ru.yandex.practicum.filmorate.validation.ValidationGroup.*;

/**
 * Требования к полям класса:
 * <p> 1. логин не может быть пустым и содержать пробелы;
 * <p> 2. электронная почта не может быть пустой и должна содержать символ @;
 * <p> 3. имя для отображения может быть пустым — в таком случае будет использован логин;
 * <p> 4. дата рождения не может быть в будущем.
 */
@Value
@Builder(toBuilder = true)
public class User {

    @Null(groups = OnCreate.class, message = "must be null")
    @NotNull(groups = OnUpdate.class, message = "must be provided")
    Long id;

    @NotEmpty
    @Pattern(regexp = "\\S+", message = "must not contain whitespaces")
    String login;

    @Email(message = "must be formatted as email")
    String email;

    String name;
    String password;

    @Past(message = "must be a date in the past")
    LocalDate birthday;

    @Builder.Default
    Set<Long> friendsId = new HashSet<>();

    public String getName() {
        return Objects.isNull(name) ? login : name;
    }

    public boolean addFriend(User user) {
        return friendsId.add(user.getId());
    }

    public boolean removeFriend(User user) {
        return friendsId.remove(user.getId());
    }
}