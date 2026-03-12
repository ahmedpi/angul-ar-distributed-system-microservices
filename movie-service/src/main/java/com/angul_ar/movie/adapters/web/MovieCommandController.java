package com.angul_ar.movie.adapters.web;

import com.angul_ar.movie.application.MovieService;
import com.angul_ar.movie.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieCommandController {
  private final MovieService movieService;

  @PostMapping
  public Movie create(@RequestBody Movie movie) {
    return movieService.createMovie(movie);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    movieService.deleteMovie(id);
  }
}
