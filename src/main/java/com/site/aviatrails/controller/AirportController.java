package com.site.aviatrails.controller;

import com.site.aviatrails.domain.Airport;
import com.site.aviatrails.exception.AirportNotFoundException;
import com.site.aviatrails.service.AirportService;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/airport")
@SecurityRequirement(name = "Bearer Authentication")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping
    public ResponseEntity<List<Airport>> getAirports() {
        List<Airport> airports = airportService.getAirports();
        if (airports.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(airports, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Airport> getAirport(@PathVariable Long id) {
        Airport airport = airportService.getAirport(id).orElseThrow(AirportNotFoundException::new);
        return new ResponseEntity<>(airport, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createAirport(@Valid @RequestBody Airport airport, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            airportService.createAirport(airport);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Airport> updateAirport(@Valid @RequestBody Airport airport, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            airportService.updateAirport(airport);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAirport(@PathVariable Long id) {
        airportService.deleteAirportById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
