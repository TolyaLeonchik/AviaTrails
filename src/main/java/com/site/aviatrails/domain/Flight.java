package com.site.aviatrails.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
@Entity(name = "flight_table")
public class Flight {

    @Id
    @SequenceGenerator(name = "SeqGenFlight", sequenceName = "travel_table_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "SeqGenFlight")
    private Long id;

    @Column(name = "airline_id")
    private Long airlineId;

    @Column(name = "from_airport_id")
    private Long fromAirportId;

    @Column(name = "to_airport_id")
    private Long toAirportId;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Column(name = "flight_price")
    private Integer flightPrice;

    @Column(name = "number_of_free_seats")
    private Integer numberOfFreeSeats;
}
