package com.site.aviatrails.domain.tickets;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
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
    private Integer ticketPrice;
    private Boolean activeStatus;
}
