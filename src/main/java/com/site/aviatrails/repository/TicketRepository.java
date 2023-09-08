package com.site.aviatrails.repository;

import com.site.aviatrails.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Ticket, Integer> {

}
