package com.angul_ar.cinema.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Cinema {
  private final Long id;
  private final String name;
  private final String location;
  private final List<Seat> seats;

  public Cinema(Long id, String name, String location,List<Seat> seats) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
    if (location == null || location.isBlank()) throw new IllegalArgumentException("Location required");
    this.id = id;
    this.name = name;
    this.location = location;
    this.seats = seats != null ? seats : new ArrayList<>();
  }

  // Business methods, e.g., updateName, relocate, etc.
  public Cinema withName(String newName) {
    return new Cinema(this.id, newName, this.location, this.seats);
  }

  // Getters
  public Long getId() { return id; }
  public String getName() { return name; }
  public String getLocation() { return location; }

  public List<Seat> getSeats() { return seats; }

  public Optional<Seat> findSeat(int seatNumber) {
    return seats.stream().filter(s -> s.getNumber() == seatNumber).findFirst();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Cinema)) return false;
    Cinema cinema = (Cinema) o;
    return Objects.equals(id, cinema.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}