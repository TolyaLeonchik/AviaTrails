package com.site.aviatrails.repository;

import com.site.aviatrails.domain.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirlinesRepository extends JpaRepository<Airline, Long> {
}
