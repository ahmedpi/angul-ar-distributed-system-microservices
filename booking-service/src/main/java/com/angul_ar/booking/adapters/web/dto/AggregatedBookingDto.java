package com.angul_ar.booking.adapters.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedBookingDto {
  private Long bookingId;
  private CinemaDto cinema;
  private MovieDto movie;
  private int seatNumber;
  private String userEmail;
  // getters/setters
}