package com.site.aviatrails.controller;

import com.site.aviatrails.domain.Flight;
import com.site.aviatrails.domain.FlightInfo;
import com.site.aviatrails.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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

    @GetMapping("/search")
    public ResponseEntity<List<FlightInfo>> getFlightByParameters(@RequestParam("cityOfDeparture") String cityOfDeparture,
                                                            @RequestParam("cityOfArrival") String cityOfArrival,
                                                            @RequestParam("date")  @DateTimeFormat(pattern="yyyy-MM-dd")Date date) {
        List<FlightInfo> flightInfoSearch = flightService.findByParameters(cityOfDeparture, cityOfArrival, date);
        if (flightInfoSearch.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(flightInfoSearch, HttpStatus.OK);
        }
    }
}
