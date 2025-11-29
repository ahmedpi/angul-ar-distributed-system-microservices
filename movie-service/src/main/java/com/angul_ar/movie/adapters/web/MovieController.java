package com.angul_ar.movie.adapters.web;

import com.angul_ar.movie.application.MovieService;
import com.angul_ar.movie.domain.Movie;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
  private final MovieService movieService;

  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @PostMapping
  public Movie create(@RequestBody Movie movie) {
    return movieService.createMovie(movie);
  }

  @GetMapping("/{id}")
  public Movie get(@PathVariable Long id) {
    return movieService.getMovie(id).orElseThrow();
  }

  @GetMapping
  public List<Movie> getAll() {
    return movieService.getAllMovies();
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    movieService.deleteMovie(id);
  }
}