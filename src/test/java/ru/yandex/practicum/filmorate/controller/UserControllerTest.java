package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.mapping.impls.UserMapperImpl;
import ru.yandex.practicum.filmorate.model.dto.UserData;
import ru.yandex.practicum.filmorate.model.dto.UserInfo;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link UserController}
 */

@WebMvcTest({UserController.class})
@Import(value = {UserMapperImpl.class})
public class UserControllerTest {

    @Value("${filmorate.endpoints.users}")
    private String path;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private UserData userData;
    private UserInfo userInfo;
    private String newUserJson;
    private String updateUserJson;

    @BeforeEach
    public void setup() {
        this.userData = UserData.builder().id(1L).email("ex").login("asd").birthday(LocalDate.of(2000, 1, 1)).build();
        this.userInfo = new UserInfo();
        this.newUserJson = "{\"login\":\"qwe\",\"email\":\"test@email.com\",\"birthday\":\"2000-12-01\"}";
        this.updateUserJson = "{\"id\":1,\"login\":\"qwe\",\"email\":\"test@email.com\",\"birthday\":\"2000-12-01\"}";
    }

    @Test
    void canTakePOSTRequestForCreateUserThanHttpStatusIsOK() throws Exception {
        when(userService.create(any())).thenReturn(userData);

        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(newUserJson))
                .andExpect(status().isCreated());
    }

    @Test
    void canTakePUTRequestForUpdateUserThanHttpStatusIsOK() throws Exception {
        when(userService.update(any())).thenReturn(userData);

        mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content(updateUserJson))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeGETRequestForGetAllUsersThanHttpStatusIsOK() throws Exception {
        when(userService.list()).thenReturn(List.of(userData));

        mockMvc.perform(get(path))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeGETRequestForGetUserByIdThanHttpStatusIsOK() throws Exception {
        when(userService.getById(anyLong())).thenReturn(userInfo);

        mockMvc.perform(get(path + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    void canValidateRequestBody() throws Exception {
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(updateUserJson))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content(newUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void canHandleConstraintViolationExceptionThanHttpStatusIsBadRequest() throws Exception {
        when(userService.create(any())).thenThrow(new ConstraintViolationException("constraint", new HashSet<>()));
        when(userService.update(any())).thenThrow(new ConstraintViolationException("constraint", new HashSet<>()));

        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(newUserJson))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content(updateUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void canHandleRuntimeExceptionThanHttpStatusIsInternalServerError() throws Exception {
        when(userService.update(any())).thenThrow(new RuntimeException("Runtime exception"));

        mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content(updateUserJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void canHandleExistExceptionThanHttpStatusIsNotFound() throws Exception {
        when(userService.update(any())).thenThrow(new ExistException("Not found"));

        mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content(updateUserJson))
                .andExpect(status().isNotFound());
    }
}