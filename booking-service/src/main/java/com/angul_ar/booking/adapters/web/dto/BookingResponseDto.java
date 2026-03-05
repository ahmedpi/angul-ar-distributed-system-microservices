package com.angul_ar.booking.adapters.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingResponseDto {
  private Long id;
  private Long cinemaId;
  private Long movieId;
  private String userEmail;
  private int seatNumber;
}
