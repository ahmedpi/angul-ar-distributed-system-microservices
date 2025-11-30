package com.angul_ar.booking.application;

import com.angul_ar.booking.application.port.BookingRepository;
import com.angul_ar.booking.domain.Booking;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class BookingService {

  private final BookingRepository bookingRepository;
  @Autowired
  private WebClient.Builder webClientBuilder;

  public Booking createBooking(Booking booking) {
    Boolean seatAvailable = webClientBuilder.build().get()
        .uri("http://cinema-service/cinemas/{cinemaId}/seats/{seatNumber}/available", booking.getCinemaId(), booking.getSeatNumber())
        .retrieve()
        .bodyToMono(Boolean.class)
        .block();

    if (!Boolean.TRUE.equals(seatAvailable)) {
      throw new IllegalStateException("Seat not available");
    }

    // ... movie availability check and booking logic ...
    return bookingRepository.save(booking);
  }

  public Optional<Booking> getBooking(Long id) {
    return bookingRepository.findById(id);
  }

  public List<Booking> getAllBookings() {
    return bookingRepository.findAll();
  }

  public void deleteBooking(Long id) {
    bookingRepository.deleteById(id);
  }
}