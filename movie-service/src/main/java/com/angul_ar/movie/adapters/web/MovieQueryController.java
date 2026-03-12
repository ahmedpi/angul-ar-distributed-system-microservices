package com.angul_ar.movie.adapters.web;

import com.angul_ar.movie.application.MovieService;
import com.angul_ar.movie.domain.Movie;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieQueryController {
  private final MovieService movieService;

  @GetMapping("/{id}")
  public Movie get(@PathVariable Long id) {
    return movieService.getMovie(id).orElseThrow();
  }

  @GetMapping
  public List<Movie> getAll() {
    return movieService.getAllMovies();
  }

}
