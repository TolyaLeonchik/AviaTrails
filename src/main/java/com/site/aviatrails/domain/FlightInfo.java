package com.site.aviatrails.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightInfo {
    private String airline;
    private String portCityFrom;
    private String portNameFrom;
    private String airportCodeFrom;
    private String portCityTo;
    private String portNameTo;
    private String airportCodeTo;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Double flightDurationInHours;
    private Integer flightPrice;
    private Integer numberOfFreeSeats;
}
