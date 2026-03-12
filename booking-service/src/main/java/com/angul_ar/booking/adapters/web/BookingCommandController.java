package com.angul_ar.booking.adapters.web;

import com.angul_ar.booking.adapters.web.dto.BookingRequestDto;
import com.angul_ar.booking.adapters.web.dto.BookingResponseDto;
import com.angul_ar.booking.application.BookingService;
import com.angul_ar.booking.domain.Booking;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingCommandController {

  private final BookingService bookingService;

  @PostMapping
  public ResponseEntity<BookingResponseDto> create(@RequestBody BookingRequestDto dto) {
    Booking booking = new Booking(null, dto.getCinemaId(), dto.getMovieId(), dto.getUserEmail(),
        dto.getSeatNumber());
    Booking created = bookingService.createBooking(booking);
    BookingResponseDto response = new BookingResponseDto(created.getId(), created.getCinemaId(),
        created.getMovieId(),
        created.getUserEmail(), created.getSeatNumber());
    return ResponseEntity.created(URI.create("/bookings" + created.getId())).body(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    bookingService.deleteBooking(id);
    return ResponseEntity.noContent().build();
  }
}
