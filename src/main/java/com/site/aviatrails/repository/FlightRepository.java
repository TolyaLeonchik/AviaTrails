package com.site.aviatrails.repository;

import com.site.aviatrails.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Query("select a.id FROM flight_table a WHERE a.fromAirportId=:from AND a.toAirportId=:to")
    Long findIdByFromAirportIdAndToAirportId(Long from, Long to);

    @Query("select a.numberOfFreeSeats FROM flight_table a WHERE a.id=:id")
    Integer findNumberOfFreeSeatsById(Long id);

    @Query("select a.flightPrice FROM flight_table a WHERE a.id=:id")
    Long findFlightPriceById(Long id);

    @Modifying
    @Query("UPDATE flight_table a SET a.numberOfFreeSeats=:updateNumber WHERE a.id=:id")
    Integer updateNumberOfFreeSeatsById(@Param("updateNumber") Integer updateNumber, @Param("id") Long id);

    @Query("select a.fromAirportId FROM flight_table a WHERE a.id=:flightId")
    Long findFromAirportIdById(long flightId);

    @Query("select a.toAirportId FROM flight_table a WHERE a.id=:flightId")
    Long findToAirportIdById(long flightId);

    @Query("select a.airlineId FROM flight_table a WHERE a.id=:flightId")
    Long findAirlineById(long flightId);
}
