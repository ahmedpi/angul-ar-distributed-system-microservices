// adapters/web/CinemaController.java
package com.angul_ar.cinema.adapters.web;

import com.angul_ar.cinema.adapters.web.dto.CinemaRequestDto;
import com.angul_ar.cinema.application.CinemaService;
import com.angul_ar.cinema.domain.Cinema;
import com.angul_ar.cinema.domain.Seat;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cinemas")
public class CinemaController {

  private final CinemaService cinemaService;

  public CinemaController(CinemaService cinemaService) {
    this.cinemaService = cinemaService;
  }

  @PostMapping
  public ResponseEntity<Cinema> create(@RequestBody CinemaRequestDto cinemaRequestDto) {
    List<Seat> seats = cinemaRequestDto.seats.stream()
        .map(s -> new Seat(s.number, s.available))
        .toList();
    Cinema cinema = new Cinema(null, cinemaRequestDto.name, cinemaRequestDto.location, seats);
    Cinema created = cinemaService.createCinema(cinema);
    return ResponseEntity.status(201).body(created);
  }

  @GetMapping("/{id}")
  public Cinema get(@PathVariable Long id) {
    return cinemaService.getCinema(id).orElseThrow();
  }

  @GetMapping
  public List<Cinema> getAll() {
    return cinemaService.getAllCinemas();
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    cinemaService.deleteCinema(id);
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