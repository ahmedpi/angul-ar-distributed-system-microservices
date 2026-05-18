package com.angul_ar.booking.adapters.messaging;

import com.angul_ar.booking.application.BookingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoyaltyEventListener {

  private static final Logger logger = LoggerFactory.getLogger(LoyaltyUpdateFailedEvent.class);
  private final BookingService bookingService;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @RabbitListener(queues = "loyalty.update.failed.queue")
  public void handleLoyaltyUpdateFailed(LoyaltyUpdateFailedEvent event) {
    logger.warn("Loyalty update failed for booking: {}. Reverting booking.", event.getBookingId());
    bookingService.cancelBooking(event.getBookingId(), false);
  }
}
