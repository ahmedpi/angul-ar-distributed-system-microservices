package com.angul_ar.booking.adapters.web;

import com.angul_ar.booking.adapters.web.dto.BookingRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private WebClient.Builder webClientBuilder;

  @Test
  void createAndGetBooking() throws Exception {
    // Mock the WebClient chain
    WebClient webClient = Mockito.mock(WebClient.class);
    WebClient.RequestHeadersUriSpec getSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
    WebClient.RequestHeadersSpec uriSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
    WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

    Mockito.when(webClientBuilder.build()).thenReturn(webClient);
    Mockito.when(webClient.get()).thenReturn(getSpec);
    Mockito.when(getSpec.uri(Mockito.anyString(), Mockito.any(Object[].class))).thenReturn(uriSpec);
    Mockito.when(uriSpec.retrieve()).thenReturn(responseSpec);
    Mockito.when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(true));

    BookingRequestDto dto = new BookingRequestDto(1L, 2L, "abc@abc.com", 5);

    mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.cinemaId").value(1L))
        .andExpect(MockMvcResultMatchers.jsonPath("$.movieId").value(2L))
        .andExpect(MockMvcResultMatchers.jsonPath("$.seatNumber").value(5))
        .andReturn().getResponse().getContentAsString();
  }
}
