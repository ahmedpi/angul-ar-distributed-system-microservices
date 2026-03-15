package com.angul_ar.booking.adapters.messaging;

import lombok.Data;

@Data
public class BookingCreatedEvent {
  private String userEmail;
  private Long bookingId;
  private Long cinemaId;
  private Long movieId;
  private Integer seatNumber;
}
