package com.angul_ar.booking.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataBookingRepository extends JpaRepository<BookingJpaEntity, Long> {}