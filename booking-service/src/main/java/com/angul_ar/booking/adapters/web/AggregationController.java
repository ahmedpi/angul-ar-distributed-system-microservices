package com.angul_ar.booking.adapters.web;

import com.angul_ar.booking.adapters.web.dto.AggregatedBookingDto;
import com.angul_ar.booking.application.AggregationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aggregated-bookings")
@RequiredArgsConstructor
public class AggregationController {

  private final AggregationService aggregationService;

  @GetMapping
  public List<AggregatedBookingDto> getAggregatedBookings() {
    return aggregationService.getAggregatedBookings();
  }
}