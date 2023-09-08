package com.site.aviatrails.domain.tickets;

import lombok.Data;

@Data
public class BookingTicketDTO {
    private String airline;
    private String portCityFrom;
    private String portCityTo;
    private String portNameFrom;
    private String portNameTo;
    private String firstName;
    private String lastName;
    private int countOfTickets;
}
