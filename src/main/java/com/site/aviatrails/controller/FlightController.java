package com.site.aviatrails.controller;

import com.site.aviatrails.domain.Flight;
import com.site.aviatrails.domain.FlightInfo;
import com.site.aviatrails.service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flights")
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        List<Flight> allFlights = flightService.getAllFlights();
        if (allFlights.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(allFlights, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightInfo> getFlightById(@PathVariable Long id) {
        FlightInfo flightById = flightService.findById(id);
        if (flightById == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(flightById, HttpStatus.OK);
        }
    }
}
