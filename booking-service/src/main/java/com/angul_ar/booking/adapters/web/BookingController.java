package com.angul_ar.booking.adapters.web;

import com.angul_ar.booking.application.BookingService;
import com.angul_ar.booking.domain.Booking;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
  private final BookingService bookingService;

  public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @PostMapping
  public Booking create(@RequestBody Booking booking) {
    return bookingService.createBooking(booking);
  }

  @GetMapping("/{id}")
  public Booking get(@PathVariable Long id) {
    return bookingService.getBooking(id).orElseThrow();
  }

  @GetMapping
  public List<Booking> getAll() {
    return bookingService.getAllBookings();
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    bookingService.deleteBooking(id);
  }
}