package com.site.aviatrails.controller;

import com.site.aviatrails.domain.Flight;
import com.site.aviatrails.service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getTickets() {
        List<Flight> allFlights = flightService.getAllFlights();
        if (allFlights.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(allFlights, HttpStatus.OK);
        }
    }
}
