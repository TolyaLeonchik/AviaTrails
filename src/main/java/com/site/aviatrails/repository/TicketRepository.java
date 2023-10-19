package com.site.aviatrails.repository;

import com.site.aviatrails.domain.tickets.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM ticket_table t WHERE t.passengerId=:id")
    List<Ticket> findByPassengerId(Long id);

    @Query("SELECT t.id FROM ticket_table t WHERE t.passengerId=:id")
    List<Long> findIdsByPassengerId(Long id);

    @Query("SELECT t, f, y, p, q from ticket_table t " +
            "JOIN flight_table f ON t.flightId = f.id " +
            "JOIN airlines y ON f.airlineId = y.id " +
            "JOIN airports p ON f.fromAirportId = p.id " +
            "JOIN airports q ON f.toAirportId = q.id WHERE t.passengerId =:userId")
    List<Object[]> getTicketWithDetails(Long userId);

    @Query("SELECT t FROM ticket_table t WHERE t.bookingExpirationTime < :currentTime AND  t.activeStatus = false")
    List<Ticket> findByBookingExpirationTimeBefore(@Param("currentTime") LocalDateTime currentTime);
}
