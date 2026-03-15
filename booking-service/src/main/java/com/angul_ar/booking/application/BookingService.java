package com.angul_ar.booking.application;

import com.angul_ar.booking.adapters.messaging.BookingCreatedEvent;
import com.angul_ar.booking.application.port.BookingRepository;
import com.angul_ar.booking.domain.Booking;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class BookingService {

  private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

  private final BookingRepository bookingRepository;
  @Autowired
  private WebClient.Builder webClientBuilder;
  @Autowired
  private RabbitTemplate rabbitTemplate;

  public Booking createBooking(Booking booking) {
    logger.info("Attempting to create booking: {}", booking);
    Boolean seatAvailable = webClientBuilder.build().get()
        .uri("http://cinema-service/cinemas/{cinemaId}/seats/{seatNumber}/available",
            booking.getCinemaId(), booking.getSeatNumber())
        .retrieve()
        .bodyToMono(Boolean.class)
        .block();

    if (!Boolean.TRUE.equals(seatAvailable)) {
      throw new IllegalStateException("Seat not available");
    }

    // ... movie availability check and booking logic ...
    Booking saved = bookingRepository.save(booking);

    BookingCreatedEvent event = new BookingCreatedEvent();
    event.setUserEmail(saved.getUserEmail());
    event.setBookingId(saved.getId());
    event.setCinemaId(saved.getCinemaId());
    event.setMovieId(saved.getMovieId());
    event.setSeatNumber(saved.getSeatNumber());

    // Publish event
    rabbitTemplate.convertAndSend("booking.exchange", "booking.created", event);

    logger.info("Booking created successfully: {}", saved.getId());
    return saved;
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