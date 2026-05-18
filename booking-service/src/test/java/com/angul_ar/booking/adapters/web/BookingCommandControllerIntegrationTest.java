package com.angul_ar.booking.adapters.web;

import com.angul_ar.booking.adapters.web.dto.BookingRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingCommandControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private RabbitTemplate rabbitTemplate;

  private static MockWebServer mockWebServer;

  @BeforeAll
  static void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
  }

  @AfterAll
  static void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  // Dynamically set the cinema.service.url property for the test context
  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    String baseUrl = mockWebServer.url("/").toString();
    registry.add("cinema.service.url", () -> baseUrl);
    registry.add("movie.service.url", () -> baseUrl);
  }


  @Test
  void createAndGetBooking() throws Exception {
// Enqueue Cinema response for /cinemas/1
    mockWebServer.enqueue(new MockResponse()
        .setBody("{\"id\":1,\"name\":\"Test Cinema\",\"location\":\"Test Address\"}")
        .addHeader("Content-Type", "application/json"));

// Enqueue Seat availability response for /cinemas/1/seats/5/available
    mockWebServer.enqueue(new MockResponse()
        .setBody("true")
        .addHeader("Content-Type", "application/json"));

//// Enqueue Movie response for /movies/2 (if your code calls this)
//    mockWebServer.enqueue(new MockResponse()
//        .setBody("{\"id\":2,\"title\":\"Test Movie\",\"genre\":\"Action\",\"duration\":120}")
//        .addHeader("Content-Type", "application/json"));

    BookingRequestDto dto = new BookingRequestDto(1L, 2L, "abc@abc.com", 5);

    try {
      mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
              .with(SecurityMockMvcRequestPostProcessors.csrf())
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(MockMvcResultMatchers.status().isCreated())
          .andExpect(MockMvcResultMatchers.jsonPath("$.cinemaId").value(1L))
          .andExpect(MockMvcResultMatchers.jsonPath("$.movieId").value(2L))
          .andExpect(MockMvcResultMatchers.jsonPath("$.seatNumber").value(5))
          .andReturn().getResponse().getContentAsString();
    } catch (Exception e) {
      System.out.println("Exception during mockMvc.perform: " + e.getMessage());
    }

    // Now, retrieve and print all requests that were made
    var request1 = mockWebServer.takeRequest(1, java.util.concurrent.TimeUnit.SECONDS);
    System.out.println("Request 1 path: " + (request1 != null ? request1.getPath() : "not made"));

    var request2 = mockWebServer.takeRequest(1, java.util.concurrent.TimeUnit.SECONDS);
    System.out.println("Request 2 path: " + (request2 != null ? request2.getPath() : "not made"));
  }

}
