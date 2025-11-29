package com.angul_ar.movie.adapters.persistence;

import com.angul_ar.movie.application.port.MovieRepository;
import com.angul_ar.movie.domain.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MovieRepositoryImpl implements MovieRepository {
  private final SpringDataMovieRepository jpaRepository;

  public MovieRepositoryImpl(SpringDataMovieRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Movie save(Movie movie) {
    MovieJpaEntity entity = new MovieJpaEntity(movie.getId(), movie.getTitle(), movie.getGenre(), movie.getDuration());
    MovieJpaEntity saved = jpaRepository.save(entity);
    return new Movie(saved.getId(), saved.getTitle(), saved.getGenre(), saved.getDuration());
  }

  @Override
  public Optional<Movie> findById(Long id) {
    return jpaRepository.findById(id)
        .map(e -> new Movie(e.getId(), e.getTitle(), e.getGenre(), e.getDuration()));
  }

  @Override
  public List<Movie> findAll() {
    return jpaRepository.findAll().stream()
        .map(e -> new Movie(e.getId(), e.getTitle(), e.getGenre(), e.getDuration()))
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    jpaRepository.deleteById(id);
  }
}