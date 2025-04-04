package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.service.FriendService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link FriendController}
 */

@WebMvcTest({FriendController.class})
public class FriendControllerTest {

    @Value("${filmorate.endpoints.users}")
    private String path;

    @MockBean
    private FriendService friendService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void canTakePUTRequestForAddToFriendThanHttpStatusIsOK() throws Exception {
        String putMapping = "/123/friends/213";
        doNothing().when(friendService).addToFriends(anyLong(), anyLong());

        mockMvc.perform(put(path + putMapping))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeDELETERequestForRemoveFromFriendThanHttpStatusIsOK() throws Exception {
        String deleteMapping = "/123/friends/213";
        doNothing().when(friendService).removeFromFriends(anyLong(), anyLong());

        mockMvc.perform(delete(path + deleteMapping))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeGETRequestForListOfFriendsThanHttpStatusIsOK() throws Exception {
        String getMapping = "/123/friends";
        when(friendService.listFriends(anyLong())).thenReturn(List.of());

        mockMvc.perform(get(path + getMapping))
                .andExpect(status().isOk());
    }

    @Test
    void canTakeGETRequestForListCommonFriendsThanHttpStatusIsOK() throws Exception {
        String getMapping = "/123/friends/common/321";
        when(friendService.listCommonFriends(anyLong(),anyLong())).thenReturn(List.of());

        mockMvc.perform(get(path + getMapping))
                .andExpect(status().isOk());
    }

    @Test
    void canHandleRuntimeExceptionThanHttpStatusIsInternalServerError() throws Exception {
        String getMapping = "/123/friends/common/321";
        when(friendService.listCommonFriends(anyLong(),anyLong())).thenThrow(new RuntimeException("exception"));

        mockMvc.perform(get(path + getMapping))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void canHandleExistExceptionThanHttpStatusIsNotFound() throws Exception {
        String getMapping = "/123/friends/common/321";
        when(friendService.listCommonFriends(anyLong(),anyLong())).thenThrow(new ExistException("Not exist"));

        mockMvc.perform(get(path + getMapping))
                .andExpect(status().isNotFound());
    }
}