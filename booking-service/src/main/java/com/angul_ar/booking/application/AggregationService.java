package com.angul_ar.booking.application;

import com.angul_ar.booking.adapters.web.dto.AggregatedBookingDto;
import com.angul_ar.booking.adapters.web.dto.CinemaDto;
import com.angul_ar.booking.adapters.web.dto.MovieDto;
import com.angul_ar.booking.application.port.BookingRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AggregationService {

  private final BookingRepository bookingRepository;
  private final CinemaClient cinemaClient;
  private final MovieClient movieClient;

  public List<AggregatedBookingDto> getAggregatedBookings() {
    return bookingRepository.findAll().stream().map(booking -> {
      CinemaDto cinema = cinemaClient.getCinema(booking.getCinemaId());
      MovieDto movie = movieClient.getMovie(booking.getMovieId());

      return new AggregatedBookingDto(
          booking.getId(),
          cinema,
          movie,
          booking.getSeatNumber(),
          booking.getUserEmail()
      );
    }).collect(Collectors.toList());
  }
}