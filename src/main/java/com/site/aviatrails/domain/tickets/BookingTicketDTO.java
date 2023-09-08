package com.site.aviatrails.domain;

import lombok.Data;

@Data
public class BookingTicketDTO {
    private String airline;
    private String portCityFrom;
    private String portCityTo;
    private String firstName;
    private String lastName;
    private int countOfTickets;
}
