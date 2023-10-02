package com.site.aviatrails.domain.tickets;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingTicketDTO {
    private String firstName;
    private String lastName;
    private String airline;
    private String portCityFrom;
    private String portCityTo;
    private String portNameFrom;
    private String portNameTo;
    private LocalDateTime departureTime;
    private int countOfTickets;
    private boolean returnTicket;

    private String returnAirline;
    private String returnPortCityFrom;
    private String returnPortCityTo;
    private String returnPortNameFrom;
    private String returnPortNameTo;
    private LocalDateTime returnDepartureTime;
    private int returnCountOfTickets;
}
