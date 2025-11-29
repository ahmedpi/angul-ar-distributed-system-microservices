// adapters/web/CinemaController.java
package com.angul_ar.cinema.adapters.web;

import com.angul_ar.cinema.application.CinemaService;
import com.angul_ar.cinema.domain.Cinema;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cinemas")
public class CinemaController {
  private final CinemaService cinemaService;

  public CinemaController(CinemaService cinemaService) {
    this.cinemaService = cinemaService;
  }

  @PostMapping
  public Cinema create(@RequestBody Cinema cinema) {
    return cinemaService.createCinema(cinema);
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
}