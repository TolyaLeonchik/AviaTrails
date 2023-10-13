package com.site.aviatrails.domain.tickets;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Entity(name = "ticket_table")
public class Ticket {

    @Id
    @SequenceGenerator(name = "SeqGenTicket", sequenceName = "ticket_table_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "SeqGenTicket")
    private Long id;

    @Column(name = "flight_id")
    private Long flightId;

    @Column(name = "passenger_id")
    private Long passengerId;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Column(name = "number_of_tickets")
    private Integer numberOfTickets;

    @Column(name = "ticket_price")
    private Integer ticketPrice;

    @Column(name = "active_status")
    private Boolean activeStatus;
}
