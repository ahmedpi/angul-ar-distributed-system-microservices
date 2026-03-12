package com.angul_ar.booking.application;

import com.angul_ar.booking.adapters.web.dto.AggregatedBookingDto;
import com.angul_ar.booking.adapters.web.dto.CinemaDto;
import com.angul_ar.booking.adapters.web.dto.MovieDto;
import com.angul_ar.booking.application.port.BookingRepository;
import com.angul_ar.booking.domain.Booking;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AggregationService {
  private final BookingRepository bookingRepository;
  private final WebClient.Builder webClientBuilder;

  public AggregationService(BookingRepository bookingRepository, WebClient.Builder webClientBuilder) {
    this.bookingRepository = bookingRepository;
    this.webClientBuilder = webClientBuilder;
  }

  public List<AggregatedBookingDto> getAggregatedBookings() {
    List<Booking> bookings = bookingRepository.findAll();
    WebClient webClient = webClientBuilder.build();

    return bookings.stream().map(booking -> {
      CinemaDto cinema = webClient.get()
          .uri("http://cinema-service/cinemas/{id}", booking.getCinemaId())
          .retrieve()
          .bodyToMono(CinemaDto.class)
          .block();

      MovieDto movie = webClient.get()
          .uri("http://movie-service/movies/{id}", booking.getMovieId())
          .retrieve()
          .bodyToMono(MovieDto.class)
          .block();

      AggregatedBookingDto dto = new AggregatedBookingDto();
      dto.setBookingId(booking.getId());
      dto.setCinema(cinema);
      dto.setMovie(movie);
      dto.setSeatNumber(booking.getSeatNumber());
      dto.setUserEmail(booking.getUserEmail());
      return dto;
    }).collect(Collectors.toList());
  }
}