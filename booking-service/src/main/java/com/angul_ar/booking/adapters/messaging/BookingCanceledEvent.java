package com.angul_ar.booking.adapters.messaging;

import lombok.Data;

@Data
public class BookingCanceledEvent {
  private String userEmail;
  private Long bookingId;
}
