package com.angul_ar.movie.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMovieRepository extends JpaRepository<MovieJpaEntity, Long> {}