package com.angul_ar.cinema.adapters.web.dto;

import java.util.List;

public class CinemaRequestDto {
  public String name;
  public String location;
  public List<SeatDto> seats;

  public static class SeatDto {
    public int number;
    public boolean available;
  }
}
