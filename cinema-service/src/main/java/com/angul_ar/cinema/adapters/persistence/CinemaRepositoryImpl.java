// adapters/persistence/CinemaRepositoryImpl.java
package com.angul_ar.cinema.adapters.persistence;

import com.angul_ar.cinema.application.port.CinemaRepository;
import com.angul_ar.cinema.domain.Cinema;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

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

    List<SeatJpaEntity> seatEntities = cinema.getSeats().stream()
        .map(s -> new SeatJpaEntity(null, s.getNumber(), s.isAvailable()))
        .collect(Collectors.toList());
    entity.setSeats(seatEntities);

    CinemaJpaEntity saved = jpaRepository.save(entity);
    return CinemaMapper.toDomain(saved);
  }

  @Override
  public Optional<Cinema> findById(Long id) {
    return jpaRepository.findById(id)
        .map(CinemaMapper::toDomain);
  }

  @Override
  public List<Cinema> findAll() {
    return jpaRepository.findAll().stream()
        .map(CinemaMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    jpaRepository.deleteById(id);
  }
}