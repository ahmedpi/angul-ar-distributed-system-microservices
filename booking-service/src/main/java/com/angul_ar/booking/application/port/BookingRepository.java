package com.angul_ar.booking.application.port;

import com.angul_ar.booking.domain.Booking;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {

  Booking save(Booking booking);

  Optional<Booking> findById(Long id);

  List<Booking> findAll();

  void deleteById(Long id);
}