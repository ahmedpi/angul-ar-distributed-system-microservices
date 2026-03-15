package com.angul_ar.loyalty.adapters.messaging;

import com.angul_ar.loyalty.application.LoyaltyService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingEventListener {

  private static final Logger logger = LoggerFactory.getLogger(BookingEventListener.class);
  private final LoyaltyService loyaltyService;

  @Value("${loyalty.points-per-booking}")
  private int pointsPerBooking;

  @RabbitListener(queues = "booking.created.queue")
  public void handleBookingCreated(BookingCreatedEvent bookingEvent) {
    logger.info("Received Booking Created Event for user: {}", bookingEvent.getUserEmail());
    loyaltyService.addPoints(bookingEvent.getUserEmail(), pointsPerBooking);
    logger.info("Loyalty points updated for user: {}", bookingEvent.getUserEmail());
  }
}
