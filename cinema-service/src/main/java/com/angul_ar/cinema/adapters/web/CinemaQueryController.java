package com.angul_ar.cinema.adapters.web;

import com.angul_ar.cinema.application.CinemaService;
import com.angul_ar.cinema.domain.Cinema;
import com.angul_ar.cinema.domain.Seat;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cinemas")
@RequiredArgsConstructor
public class CinemaQueryController {
  private final CinemaService cinemaService;

  @GetMapping
  public List<Cinema> getAll() {
    return cinemaService.getAllCinemas();
  }

  @GetMapping("/{id}")
  public Cinema get(@PathVariable Long id) {
    return cinemaService.getCinema(id).orElseThrow();
  }

  @GetMapping("/{cinemaId}/seats/{seatNumber}/available")
  public ResponseEntity<Boolean> isSeatAvailable(@PathVariable Long cinemaId,
      @PathVariable int seatNumber) {
    Optional<Cinema> optionalCinema = cinemaService.getCinema(cinemaId);
    if (optionalCinema.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    Cinema cinema = optionalCinema.get();
    Optional<Seat> seat = cinema.findSeat(seatNumber);
    return seat.map(s -> ResponseEntity.ok(s.isAvailable()))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
