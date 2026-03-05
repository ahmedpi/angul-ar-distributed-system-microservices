package com.angul_ar.cinema.application;

import com.angul_ar.cinema.adapters.persistence.CinemaJpaEntity;
import com.angul_ar.cinema.adapters.persistence.CinemaMapper;
import com.angul_ar.cinema.adapters.persistence.SeatJpaEntity;
import com.angul_ar.cinema.application.port.CinemaRepository;
import com.angul_ar.cinema.domain.Cinema;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CinemaService {

  private static final Logger logger = LoggerFactory.getLogger(CinemaService.class);

  private final CinemaRepository cinemaRepository;

  public CinemaService(CinemaRepository cinemaRepository) {
    this.cinemaRepository = cinemaRepository;
  }

  public Cinema createCinema(Cinema cinema) {
    logger.info("Attempting to create cinema: {}", cinema);
    CinemaJpaEntity cinemaJpa = new CinemaJpaEntity();
    cinemaJpa.setName(cinema.getName());
    cinemaJpa.setLocation(cinema.getLocation());
    List<SeatJpaEntity> seatJpaList = cinema.getSeats().stream()
        .map(s -> new SeatJpaEntity(null, s.getNumber(), s.isAvailable()))
        .toList();
    cinemaJpa.setSeats(seatJpaList);
    Cinema saved = cinemaRepository.save(CinemaMapper.toDomain(cinemaJpa));
    logger.info("Cinema created successfully: {}", saved.getId());
    return saved;
  }

  public Optional<Cinema> getCinema(Long id) {
    return cinemaRepository.findById(id);
  }

  public List<Cinema> getAllCinemas() {
    return cinemaRepository.findAll();
  }

  public void deleteCinema(Long id) {
    cinemaRepository.deleteById(id);
  }
}