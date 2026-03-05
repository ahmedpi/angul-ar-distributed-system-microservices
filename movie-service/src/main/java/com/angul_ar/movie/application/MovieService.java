package com.angul_ar.movie.application;

import com.angul_ar.movie.application.port.MovieRepository;
import com.angul_ar.movie.domain.Movie;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

  private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

  private final MovieRepository movieRepository;

  public MovieService(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  public Movie createMovie(Movie movie) {
    logger.info("Attempting to create movie: {}", movie);
    Movie saved = movieRepository.save(movie);
    logger.info("Movie created successfully: {}", saved.getId());
    return saved;
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