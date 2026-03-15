package com.angul_ar.loyalty.application;

import com.angul_ar.loyalty.application.port.LoyaltyAccountRepository;
import com.angul_ar.loyalty.domain.LoyaltyAccount;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoyaltyService {

  @Autowired
  private final LoyaltyAccountRepository repository;

  @Transactional
  public void addPoints(String userEmail, int points) {
    LoyaltyAccount account = repository.findById(userEmail)
        .orElseGet(() -> {
          LoyaltyAccount acc = new LoyaltyAccount();
          acc.setEmail(userEmail);
          acc.setPoints(0);
          return acc;
        });

    account.setPoints(points);
    repository.save(account);
  }

  public Optional<LoyaltyAccount> getPoints(String userEmail) {
    return repository.findById(userEmail);
  }
}
