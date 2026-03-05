package com.angul_ar.cinema.adapters.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.angul_ar.cinema.adapters.persistence.SpringDataCinemaRepository;
import com.angul_ar.cinema.adapters.web.dto.CinemaRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CinemaControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private SpringDataCinemaRepository cinemaRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void createAndGetCinema() throws Exception {
    CinemaRequestDto dto = new CinemaRequestDto();
    dto.name = "Integration Cinema";
    dto.location = "Integration Location";
    dto.seats = List.of(new CinemaRequestDto.SeatDto(1, true));

    // Create cinema
    String response = mockMvc.perform(post("/cinemas")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    // Parse ID from response (if needed) and fetch
    mockMvc.perform(get("/cinemas"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Integration Cinema"));
  }
}
