package com.angul_ar.booking.application;

import com.angul_ar.booking.application.port.BookingRepository;
import com.angul_ar.booking.domain.Booking;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

  private final BookingRepository bookingRepository;

  public BookingService(BookingRepository bookingRepository) {
    this.bookingRepository = bookingRepository;
  }

  public Booking createBooking(Booking booking) {
    // Add business logic here (e.g., check seat availability)
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