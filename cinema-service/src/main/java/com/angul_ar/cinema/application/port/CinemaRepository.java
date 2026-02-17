package com.angul_ar.cinema.application.port;

import com.angul_ar.cinema.domain.Cinema;
import java.util.List;
import java.util.Optional;

public interface CinemaRepository {
  Cinema save(Cinema cinema);
  Optional<Cinema> findById(Long id);
  List<Cinema> findAll();
  void deleteById(Long id);
}