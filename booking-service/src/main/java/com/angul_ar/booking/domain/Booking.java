package com.angul_ar.booking.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

  private Long id;
  private Long cinemaId;
  private Long movieId;
  private String userEmail;
  private int seatNumber;
  private BookingStatus status;

  public static Booking create(Long cinemaId, Long movieId, String userEmail, int seatNumber) {
    Booking booking = new Booking();
    booking.cinemaId = cinemaId;
    booking.movieId = movieId;
    booking.userEmail = userEmail;
    booking.seatNumber = seatNumber;
    booking.status = BookingStatus.CREATED;
    return booking;
  }

  public void cancel() {
    if (this.status == BookingStatus.CANCELED) {
      throw new IllegalStateException("Booking is already canceled");
    }
    this.status = BookingStatus.CANCELED;
  }
}