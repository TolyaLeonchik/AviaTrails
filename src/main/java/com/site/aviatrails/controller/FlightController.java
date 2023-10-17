package com.site.aviatrails.controller;

import com.site.aviatrails.domain.Flight;
import com.site.aviatrails.domain.FlightInfo;
import com.site.aviatrails.exception.FlightNotFoundException;
import com.site.aviatrails.service.FlightService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/flights")
@SecurityRequirement(name = "Bearer Authentication")
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

    @GetMapping("/info/{id}")
    public ResponseEntity<FlightInfo> getFlightInfoById(@PathVariable Long id) {
        FlightInfo flightById = flightService.findInfoById(id);
        if (flightById == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(flightById, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        Flight flight = flightService.findById(id).orElseThrow(FlightNotFoundException::new);
        return new ResponseEntity<>(flight, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightInfo>> getFlightByParameters(@RequestParam("cityOfDeparture") String cityOfDeparture,
                                                                  @RequestParam("cityOfArrival") String cityOfArrival,
                                                                  @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                  LocalDate date) {
        List<FlightInfo> flightInfoSearch = flightService.findByParameters(cityOfDeparture, cityOfArrival, date);
        if (flightInfoSearch.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(flightInfoSearch, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createFlight(@Valid @RequestBody Flight flight, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            flightService.createFlight(flight);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateFlight(@Valid @RequestBody Flight flight, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            flightService.updateFlight(flight);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteFlightById(@PathVariable Long id) {
        flightService.deleteFlightById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
