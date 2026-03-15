package com.angul_ar.loyalty.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class LoyaltyAccount {
  @Id
  private String email;
  private int points;
}
