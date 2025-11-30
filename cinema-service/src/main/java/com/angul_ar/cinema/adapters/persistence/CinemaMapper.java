package com.angul_ar.cinema.adapters.persistence;

import com.angul_ar.cinema.domain.Cinema;
import com.angul_ar.cinema.domain.Seat;
import java.util.List;
import java.util.stream.Collectors;

public class CinemaMapper {

  public static Cinema toDomain(CinemaJpaEntity entity) {
    if (entity == null) {
      return null;
    }
    List<Seat> seats = entity.getSeats() != null
        ? entity.getSeats().stream().map(CinemaMapper::toDomain).collect(Collectors.toList())
        : List.of();
    return new Cinema(entity.getId(), entity.getName(), entity.getLocation(), seats);
  }

  public static CinemaJpaEntity toEntity(Cinema domain) {
    if (domain == null) {
      return null;
    }
    List<SeatJpaEntity> seats = domain.getSeats() != null
        ? domain.getSeats().stream().map(CinemaMapper::toEntity).collect(Collectors.toList())
        : List.of();
    CinemaJpaEntity entity = new CinemaJpaEntity();
    entity.setId(domain.getId());
    entity.setName(domain.getName());
    entity.setLocation(domain.getLocation());
    entity.setSeats(seats);
    return entity;
  }

  public static Seat toDomain(SeatJpaEntity entity) {
    if (entity == null) {
      return null;
    }
    return new Seat(entity.getNumber(), entity.isAvailable());
  }

  public static SeatJpaEntity toEntity(Seat domain) {
    if (domain == null) {
      return null;
    }
    SeatJpaEntity entity = new SeatJpaEntity();
    entity.setNumber(domain.getNumber());
    entity.setAvailable(domain.isAvailable());
    return entity;
  }
}
