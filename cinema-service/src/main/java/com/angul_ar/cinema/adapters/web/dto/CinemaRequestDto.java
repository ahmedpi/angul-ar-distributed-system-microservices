package com.angul_ar.cinema.adapters.web.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class CinemaRequestDto {
  public String name;
  public String location;
  public List<SeatDto> seats;

  @AllArgsConstructor
  @NoArgsConstructor
  public static class SeatDto {
    public int number;
    public boolean available;
  }
}
