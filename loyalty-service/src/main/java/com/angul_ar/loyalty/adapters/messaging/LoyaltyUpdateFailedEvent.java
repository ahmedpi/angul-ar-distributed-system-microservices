package com.angul_ar.loyalty.adapters.messaging;

import lombok.Data;

@Data
public class LoyaltyUpdateFailedEvent {

  private String userEmail;
  private Long bookingId;
  private String reason;
}
