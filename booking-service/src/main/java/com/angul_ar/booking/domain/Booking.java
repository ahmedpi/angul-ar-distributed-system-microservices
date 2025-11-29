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
}