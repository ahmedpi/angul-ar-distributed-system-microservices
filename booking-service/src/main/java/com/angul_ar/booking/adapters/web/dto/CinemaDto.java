package com.angul_ar.booking.adapters.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CinemaDto {

  private Long id;
  private String name;
  private String location;
}
