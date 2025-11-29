package com.angul_ar.cinema.domain;

import java.util.Objects;

public class Cinema {
  private final Long id;
  private final String name;
  private final String location;

  public Cinema(Long id, String name, String location) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
    if (location == null || location.isBlank()) throw new IllegalArgumentException("Location required");
    this.id = id;
    this.name = name;
    this.location = location;
  }

  // Business methods, e.g., updateName, relocate, etc.
  public Cinema withName(String newName) {
    return new Cinema(this.id, newName, this.location);
  }

  // Getters
  public Long getId() { return id; }
  public String getName() { return name; }
  public String getLocation() { return location; }

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