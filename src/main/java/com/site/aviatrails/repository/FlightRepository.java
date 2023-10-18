package com.site.aviatrails.repository;

import com.site.aviatrails.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("select a.id FROM flight_table a WHERE a.fromAirportId=:from AND a.toAirportId=:to AND a.departureTime=:dateTime")
    Long findIdByParameters(Long from, Long to, LocalDateTime dateTime);

    @Query("select a.numberOfFreeSeats FROM flight_table a WHERE a.id=:id")
    Integer findNumberOfFreeSeatsById(Long id);

    @Query("select a.flightPrice FROM flight_table a WHERE a.id=:id")
    Integer findFlightPriceById(Long id);

    @Modifying
    @Query("UPDATE flight_table a SET a.numberOfFreeSeats=:updateNumber WHERE a.id=:id")
    Integer updateNumberOfFreeSeatsById(@Param("updateNumber") Integer updateNumber, @Param("id") Long id);

    @Query("select a from flight_table a where a.fromAirportId in :from AND a.toAirportId in :to AND DATE(a.departureTime)=:departureDate")
    List<Flight> findByCityFromAndCityToAndLocalDate(List<Long> from, List<Long> to, LocalDate departureDate);
}
