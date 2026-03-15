package com.angul_ar.loyalty.application.port;

import com.angul_ar.loyalty.domain.LoyaltyAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyAccountRepository extends JpaRepository<LoyaltyAccount, String> {
}
