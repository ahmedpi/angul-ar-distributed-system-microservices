package com.angul_ar.loyalty.adapters.web;

import com.angul_ar.loyalty.application.LoyaltyService;
import com.angul_ar.loyalty.domain.LoyaltyAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loyalty")
@RequiredArgsConstructor
public class LoyaltyQueryController {
  private final LoyaltyService loyaltyService;

  @GetMapping("/{userEmail}")
  public LoyaltyAccount getPoints(@PathVariable String userEmail) {
    return loyaltyService.getPoints(userEmail).orElse(null);
  }
}
