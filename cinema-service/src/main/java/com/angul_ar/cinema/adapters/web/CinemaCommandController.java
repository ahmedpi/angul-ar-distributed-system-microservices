package com.angul_ar.cinema.adapters.web;

import com.angul_ar.cinema.adapters.web.dto.CinemaRequestDto;
import com.angul_ar.cinema.application.CinemaService;
import com.angul_ar.cinema.domain.Cinema;
import com.angul_ar.cinema.domain.Seat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cinemas")
@RequiredArgsConstructor
public class CinemaCommandController {

  private final CinemaService cinemaService;

  @PostMapping
  public ResponseEntity<Cinema> create(@RequestBody CinemaRequestDto cinemaRequestDto) {
    List<Seat> seats = cinemaRequestDto.seats.stream()
        .map(s -> new Seat(s.number, s.available))
        .toList();
    Cinema cinema = new Cinema(null, cinemaRequestDto.name, cinemaRequestDto.location, seats);
    Cinema created = cinemaService.createCinema(cinema);
    return ResponseEntity.status(201).body(created);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    cinemaService.deleteCinema(id);
  }

}
