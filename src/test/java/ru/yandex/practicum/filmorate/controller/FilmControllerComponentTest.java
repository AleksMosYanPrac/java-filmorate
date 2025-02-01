package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link FilmController}<br>
 * <p>
 * The test does not use @MockBean of service because <br>
 * controller is not using DI (Dependency Injection) <br>
 * which proved by Spring Framework
 * </p>
 */

@WebMvcTest({FilmController.class})
public class FilmControllerComponentTest {

    private static final String PATH = "/films";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void canTakePOSTRequestForAddFilmThanHttpStatusIsCreated() throws Exception {
        String body = "{\"id\":0,\"name\":\"film\",\"releaseDate\":\"2025-02-08\"," +
                      "\"description\":\"-\",\"duration\":10}";

        mockMvc.perform(post(PATH).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    public void canTakePUTRequestForUpdateFilmThanHttpStatusIsOK() throws Exception {
        String body = "{\"id\":0,\"name\":\"film\",\"releaseDate\":\"2025-02-08\"," +
                      "\"description\":\"-\",\"duration\":10}";

        mockMvc.perform(post(PATH).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andDo(result -> {
                    mockMvc.perform(put(PATH).content(result.getResponse().getContentAsString())
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().is(HttpStatus.OK.value()));
                });
    }

    @Test
    public void canTakeGETRequestForGetAllFilmsThanHttpStatusIsOK() throws Exception {
        mockMvc.perform(get(PATH))
                .andExpect(status().is(HttpStatus.OK.value()));
    }
}