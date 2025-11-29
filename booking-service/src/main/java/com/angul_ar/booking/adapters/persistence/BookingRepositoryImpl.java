package com.angul_ar.booking.adapters.persistence;

import com.angul_ar.booking.application.port.BookingRepository;
import com.angul_ar.booking.domain.Booking;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BookingRepositoryImpl implements BookingRepository {
  private final SpringDataBookingRepository jpaRepository;

  public BookingRepositoryImpl(SpringDataBookingRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Booking save(Booking booking) {
    BookingJpaEntity entity = new BookingJpaEntity(
        booking.getId(),
        booking.getCinemaId(),
        booking.getMovieId(),
        booking.getUserEmail(),
        booking.getSeatNumber()
    );
    BookingJpaEntity saved = jpaRepository.save(entity);
    return new Booking(
        saved.getId(),
        saved.getCinemaId(),
        saved.getMovieId(),
        saved.getUserEmail(),
        saved.getSeatNumber()
    );
  }

  @Override
  public Optional<Booking> findById(Long id) {
    return jpaRepository.findById(id)
        .map(e -> new Booking(
            e.getId(),
            e.getCinemaId(),
            e.getMovieId(),
            e.getUserEmail(),
            e.getSeatNumber()
        ));
  }

  @Override
  public List<Booking> findAll() {
    return jpaRepository.findAll().stream()
        .map(e -> new Booking(
            e.getId(),
            e.getCinemaId(),
            e.getMovieId(),
            e.getUserEmail(),
            e.getSeatNumber()
        ))
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    jpaRepository.deleteById(id);
  }
}