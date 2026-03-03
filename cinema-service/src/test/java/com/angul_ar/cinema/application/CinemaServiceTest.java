package com.angul_ar.cinema.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.angul_ar.cinema.application.port.CinemaRepository;
import com.angul_ar.cinema.domain.Cinema;
import com.angul_ar.cinema.domain.Seat;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CinemaServiceTest {

  private CinemaRepository cinemaRepository;
  private CinemaService cinemaService;

  @BeforeEach
  void setUp() {
    cinemaRepository = mock(CinemaRepository.class);
    cinemaService = new CinemaService(cinemaRepository);
  }

  @Test
  void createCinema_shouldSaveAndReturnCinema() {
    Cinema cinema = new Cinema(null, "Test Cinema", "Test Location", List.of(new Seat(1, true)));
    when(cinemaRepository.save(any())).thenReturn(cinema);

    Cinema result = cinemaService.createCinema(cinema);

    assertNotNull(result);
    assertEquals("Test Cinema", result.getName());
    verify(cinemaRepository).save(any());
  }

  @Test
  void getCinema_shouldReturnCinemaIfExists() {
    Cinema cinema = new Cinema(1L, "Test Cinema", "Test Location", List.of());
    when(cinemaRepository.findById(1L)).thenReturn(Optional.of(cinema));

    Optional<Cinema> result = cinemaService.getCinema(1L);

    assertTrue(result.isPresent());
    assertEquals("Test Cinema", result.get().getName());
  }

  @Test
  void getAllCinemas_shouldReturnList() {
    when(cinemaRepository.findAll()).thenReturn(List.of(new Cinema(1L, "A", "L", List.of())));

    List<Cinema> cinemas = cinemaService.getAllCinemas();

    assertEquals(1, cinemas.size());
  }

  @Test
  void deleteCinema_shouldCallRepository() {
    cinemaService.deleteCinema(1L);
    verify(cinemaRepository).deleteById(1L);
  }
}