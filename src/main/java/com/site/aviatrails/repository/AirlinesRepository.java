package com.site.aviatrails.repository;

import com.site.aviatrails.domain.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AirlinesRepository extends JpaRepository<Airline, Long> {
    @Query("SELECT t.airlineName FROM airlines t WHERE t.id=:airlineId")
    String findAirlineNameById(long airlineId);
    @Query("SELECT t.airportId FROM airlines t WHERE t.airlineName=:airlineName")
    Long findPortIdByAirlineName(String airlineName);
}
