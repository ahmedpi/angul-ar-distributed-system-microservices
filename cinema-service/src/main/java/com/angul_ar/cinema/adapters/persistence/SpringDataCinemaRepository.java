// adapters/persistence/SpringDataCinemaRepository.java
package com.angul_ar.cinema.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCinemaRepository extends JpaRepository<CinemaJpaEntity, Long> {}