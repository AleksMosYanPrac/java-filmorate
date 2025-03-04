package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.mapping.impls.FilmMapperImpl;
import ru.yandex.practicum.filmorate.service.RateService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link RateController}
 */

@WebMvcTest({RateController.class})
@Import(value = {FilmMapperImpl.class})
public class RateControllerTest {

    @Value("${filmorate.endpoints.films}")
    private String path;

    @MockBean
    private RateService rateService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void canTakePUTRequestForLikeFilmThanHttpStatusIsOK() throws Exception {
        String putMapping = "/123/like/321";
        doNothing().when(rateService).putLike(anyLong(), anyLong());

        mockMvc.perform(put(path + putMapping))
                .andExpect(status().isOk());
    }

    @Test
    public void canTakeDELETERequestForUnlikeFilmThanHttpStatusIsOK() throws Exception {
        String deleteMapping = "/123/like/321";
        doNothing().when(rateService).removeLike(anyLong(), anyLong());

        mockMvc.perform(delete(path + deleteMapping))
                .andExpect(status().isOk());
    }

    @Test
    public void canTakeGETRequestForGetPopularThanHttpStatusIsOK() throws Exception {
        String getMapping = "/popular";
        String requestParameter = "?count=1";
        when(rateService.listPopularFilms(anyLong())).thenReturn(List.of());

        mockMvc.perform(get(path + getMapping + requestParameter))
                .andExpect(status().isOk());
    }

    @Test
    public void canTakeGETRequestWithDefaultParamForGetPopularThanHttpStatusIsOK() throws Exception {
        String getMapping = "/popular";
        when(rateService.listPopularFilms(anyLong())).thenReturn(List.of());

        mockMvc.perform(get(path + getMapping))
                .andExpect(status().isOk());
    }

    @Test
    void canHandleRuntimeExceptionThanHttpStatusIsInternalServerError() throws Exception {
        String getMapping = "/popular";
        when(rateService.listPopularFilms(anyLong())).thenThrow(new RuntimeException("Runtime exception"));

        mockMvc.perform(get(path + getMapping))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void canHandleExistExceptionThanHttpStatusIsNotFound() throws Exception {
        String deleteMapping = "/123/like/321";
        doThrow(new ExistException("not exist")).when(rateService).removeLike(anyLong(), anyLong());

        mockMvc.perform(delete(path + deleteMapping))
                .andExpect(status().isNotFound());
    }
}