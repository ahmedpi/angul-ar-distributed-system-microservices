package com.angul_ar.booking.application;

import com.angul_ar.booking.adapters.messaging.BookingCanceledEvent;
import com.angul_ar.booking.adapters.messaging.BookingCreatedEvent;
import com.angul_ar.booking.adapters.web.dto.BookingResponseDto;
import com.angul_ar.booking.adapters.web.dto.CinemaDto;
import com.angul_ar.booking.adapters.web.mapper.BookingMapper;
import com.angul_ar.booking.application.port.BookingRepository;
import com.angul_ar.booking.domain.Booking;
import com.angul_ar.booking.domain.BookingStatus;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
  private final CinemaClient cinemaClient;

  @Autowired
  private WebClient.Builder webClientBuilder;
  @Autowired
  private RabbitTemplate rabbitTemplate;

  public Booking createBooking(Booking booking) {
    logger.info("Attempting to create booking: {}", booking);

    CinemaDto cinema = cinemaClient.getCinema(booking.getCinemaId());
    if (cinema == null) {
      throw new IllegalStateException("Cinema not found");
    }

    Boolean seatAvailable = cinemaClient.isSeatAvailable(booking.getCinemaId(), booking.getSeatNumber());
    if (!Boolean.TRUE.equals(seatAvailable)) {
      throw new IllegalStateException("Seat not available");
    }
//
//    Boolean seatAvailable = webClientBuilder.build().get()
//        .uri("http://cinema-service/cinemas/{cinemaId}/seats/{seatNumber}/available",
//            booking.getCinemaId(), booking.getSeatNumber())
//        .retrieve()
//        .bodyToMono(Boolean.class)
//        .block();
//
//    if (!Boolean.TRUE.equals(seatAvailable)) {
//      throw new IllegalStateException("Seat not available");
//    }

    // ... movie availability check and booking logic ...
    booking.setStatus(BookingStatus.CREATED);
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

  public void cancelBooking(Long bookingId, boolean emitEvent) {
    logger.info("Attempting to cancel booking with id: {}", bookingId);

    Booking booking = bookingRepository.findById(bookingId)
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("Booking with id %s not found", bookingId)));

    if (booking.getStatus() == BookingStatus.CANCELED) {
      return;
    }

    booking.cancel();
    bookingRepository.save(booking);

    if (emitEvent) {
      BookingCanceledEvent event = new BookingCanceledEvent();
      event.setUserEmail(booking.getUserEmail());
      event.setBookingId(booking.getId());
      // Publish event
      rabbitTemplate.convertAndSend("booking.exchange", "booking.canceled", event);
    }
    logger.info("Booking canceled successfully: {}", bookingId);
  }

  public Optional<BookingResponseDto> getBookingDto(Long id) {
    return bookingRepository.findById(id).map(BookingMapper::toDto);
  }

  public List<BookingResponseDto> getAllBookingDtos() {
    return bookingRepository.findAll().stream()
        .map(BookingMapper::toDto)
        .collect(Collectors.toList());
  }

  public void deleteBooking(Long id) {
    bookingRepository.deleteById(id);
  }
}