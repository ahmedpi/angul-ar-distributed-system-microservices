// adapters/persistence/CinemaRepositoryImpl.java
package com.angul_ar.cinema.adapters.persistence;

import com.angul_ar.cinema.application.port.CinemaRepository;
import com.angul_ar.cinema.domain.Cinema;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CinemaRepositoryImpl implements CinemaRepository {
  private final SpringDataCinemaRepository jpaRepository;

  public CinemaRepositoryImpl(SpringDataCinemaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Cinema save(Cinema cinema) {
    CinemaJpaEntity entity = new CinemaJpaEntity();
    entity.setName(cinema.getName());
    entity.setLocation(cinema.getLocation());
    CinemaJpaEntity saved = jpaRepository.save(entity);
    return new Cinema(saved.getId(), saved.getName(), saved.getLocation());
  }

  @Override
  public Optional<Cinema> findById(Long id) {
    return jpaRepository.findById(id)
        .map(e -> new Cinema(e.getId(), e.getName(), e.getLocation()));
  }

  @Override
  public List<Cinema> findAll() {
    return jpaRepository.findAll().stream()
        .map(e -> new Cinema(e.getId(), e.getName(), e.getLocation()))
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    jpaRepository.deleteById(id);
  }
}