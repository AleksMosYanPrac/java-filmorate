package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.ExistException;

import ru.yandex.practicum.filmorate.model.dto.FilmData;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link FilmController}
 */

@WebMvcTest({FilmController.class})
public class FilmControllerTest {

    @Value("${filmorate.endpoints.films}")
    private String path;

    @MockBean
    private FilmService filmService;

    @Autowired
    private MockMvc mockMvc;

    private FilmData filmData;
    private FilmInfo filmInfo;
    private String newFilmJson;
    private String updateFilmJson;

    @BeforeEach
    public void setup() {
        this.filmData = new FilmData(1L, "a", LocalDate.now(), "-", 10L);
        this.filmInfo = new FilmInfo(1L, "a", "2025-02-08", "-", 10L, 1L);
        this.newFilmJson = "{\"name\":\"film\",\"releaseDate\":\"2025-02-08\"," +
                           "\"description\":\"-\",\"duration\":10}";
        this.updateFilmJson = "{\"id\":1,\"name\":\"film\",\"releaseDate\":\"2025-02-08\"," +
                              "\"description\":\"-\",\"duration\":10}";
    }

    @Test
    public void canTakePOSTRequestForAddFilmThanHttpStatusIsCreated() throws Exception {
        when(filmService.add(any())).thenReturn(filmData);

        mockMvc.perform(post(path).content(newFilmJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    public void canTakeGETRequestForGetFilmByIdThanHttpStatusIsOK() throws Exception {
        when(filmService.getById(anyLong())).thenReturn(filmInfo);

        mockMvc.perform(get(path + "/1"))
                .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    public void canTakePUTRequestForUpdateFilmThanHttpStatusIsOK() throws Exception {
        when(filmService.add(any())).thenReturn(filmData);
        when(filmService.update(any())).thenReturn(filmData);

        mockMvc.perform(post(path).content(newFilmJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andDo(result ->
                        mockMvc.perform(put(path).content(result.getResponse().getContentAsString())
                                        .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().is(HttpStatus.OK.value()))
                );
    }

    @Test
    public void canTakeGETRequestForGetAllFilmsThanHttpStatusIsOK() throws Exception {
        mockMvc.perform(get(path))
                .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void canHandleConstraintViolationExceptionThanHttpStatusIsBadRequest() throws Exception {
        when(filmService.add(any())).thenThrow(new ConstraintViolationException("constraint", new HashSet<>()));
        when(filmService.update(any())).thenThrow(new ConstraintViolationException("constraint", new HashSet<>()));

        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(newFilmJson))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content(updateFilmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void canHandleRuntimeExceptionThanHttpStatusIsInternalServerError() throws Exception {
        when(filmService.update(any())).thenThrow(new RuntimeException("Runtime exception"));

        mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content(updateFilmJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void canHandleExistExceptionThanHttpStatusIsNotFound() throws Exception {
        when(filmService.update(any())).thenThrow(new ExistException("Not found"));

        mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content(updateFilmJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void canValidateRequestBody() throws Exception {
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(updateFilmJson))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content(newFilmJson))
                .andExpect(status().isBadRequest());
    }
}