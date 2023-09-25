package com.site.aviatrails.repository;

import com.site.aviatrails.domain.tickets.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM ticket_table t WHERE t.passengerId=:id")
    List<Ticket> findByPassengerId(Long id);
    @Query("SELECT t.id FROM ticket_table t WHERE t.passengerId=:id")
    Long findIdByPassengerId(Long id);
    @Query("SELECT t.id FROM ticket_table t WHERE t.passengerId=:id")
    List<Long> findIdsByPassengerId(Long id);
    @Query("SELECT t.flightId FROM ticket_table t WHERE t.id=:ticketId")
    Long findFlightIdById(Long ticketId);

    @Query("SELECT t.flightId FROM ticket_table t WHERE t.passengerId=:id")
    List<Long> findFlightIdsByIdPassenger(Long id);
}
