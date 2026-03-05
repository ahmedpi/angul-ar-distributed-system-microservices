package com.angul_ar.booking.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.angul_ar.booking.application.port.BookingRepository;
import com.angul_ar.booking.domain.Booking;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BookingServiceTest {

  BookingService bookingService;
  BookingRepository bookingRepository;
  WebClient.Builder webClientBuilder;

  @BeforeEach
  void setup() {
    bookingRepository = Mockito.mock(BookingRepository.class);
    webClientBuilder = Mockito.mock(WebClient.Builder.class);

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

    bookingService = new BookingService(bookingRepository, webClientBuilder);
  }

  @Test
  void createBooking_shouldSaveAndReturnBooking() {
    Booking booking = new Booking(null, 1L, 1L, null, 1);

    Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

    Booking savedBooking = bookingService.createBooking(booking);

    Assertions.assertEquals(1L, savedBooking.getCinemaId());
  }

  @Test
  void getBooking_shouldReturnBookingIfExists() {
    Booking booking = new Booking(1L, 1L, 1L, null, 1);

    Mockito.when(bookingRepository.findById(Mockito.any())).thenReturn(Optional.of(booking));

    Optional<Booking> result = bookingService.getBooking(1L);
    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals(1L, result.get().getId());
    Assertions.assertEquals(1L, result.get().getCinemaId());
    Assertions.assertEquals(1L, result.get().getMovieId());
    Assertions.assertEquals(1, result.get().getSeatNumber());
  }

  @Test
  void getAllBookings_shouldReturnList() {
    when(bookingRepository.findAll()).thenReturn(List.of(new Booking(null, 1L, 1L, null, 1)));

    List<Booking> bookings = bookingService.getAllBookings();

    assertEquals(1, bookings.size());
  }

  @Test
  void deleteBooking_shouldCallRepository() {
    bookingService.deleteBooking(1L);
    verify(bookingRepository).deleteById(1L);
  }
}
