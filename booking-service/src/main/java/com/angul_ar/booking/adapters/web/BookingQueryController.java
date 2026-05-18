package com.angul_ar.booking.adapters.web;

import com.angul_ar.booking.adapters.web.dto.AggregatedBookingDto;
import com.angul_ar.booking.adapters.web.dto.BookingResponseDto;
import com.angul_ar.booking.application.AggregationService;
import com.angul_ar.booking.application.BookingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingQueryController {

  private final BookingService bookingService;
  private final AggregationService aggregationService;

  @GetMapping
  public List<BookingResponseDto> getAll() {
    return bookingService.getAllBookingDtos();
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookingResponseDto> get(@PathVariable Long id) {
    return bookingService.getBookingDto(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/aggregated-bookings")
  public List<AggregatedBookingDto> getAggregatedBookings() {
    return aggregationService.getAggregatedBookings();
  }
}
