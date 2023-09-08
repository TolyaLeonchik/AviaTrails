package com.site.aviatrails.domain.tickets;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserTicketInfo {
    private String firstName;
    private String lastName;
    private String airlineName;
    private String portCityFrom;
    private String portCityTo;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer numberOfTickets;
    private Integer seatNumber;
    private Long ticketPrice;
}
