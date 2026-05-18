package com.angul_ar.booking.adapters.web.mapper;

import com.angul_ar.booking.adapters.web.dto.BookingResponseDto;
import com.angul_ar.booking.domain.Booking;

public class BookingMapper {

  public static BookingResponseDto toDto(Booking booking) {
    return new BookingResponseDto(
        booking.getId(),
        booking.getCinemaId(),
        booking.getMovieId(),
        booking.getUserEmail(),
        booking.getSeatNumber(),
        booking.getStatus()
    );
  }

}
