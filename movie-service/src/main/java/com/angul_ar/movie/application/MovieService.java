package com.angul_ar.movie.application;

import com.angul_ar.movie.domain.Movie;
import com.angul_ar.movie.application.port.MovieRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
  private final MovieRepository movieRepository;

  public MovieService(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  public Movie createMovie(Movie movie) {
    return movieRepository.save(movie);
  }

  public Optional<Movie> getMovie(Long id) {
    return movieRepository.findById(id);
  }

  public List<Movie> getAllMovies() {
    return movieRepository.findAll();
  }

  public void deleteMovie(Long id) {
    movieRepository.deleteById(id);
  }
}