package com.site.aviatrails.controller;

import com.site.aviatrails.domain.Airline;
import com.site.aviatrails.exception.AirlineNotFoundException;
import com.site.aviatrails.service.AirlineService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

import java.util.List;

@RestController
@RequestMapping("/airline")
@SecurityRequirement(name = "Bearer Authentication")
public class AirlineController {

    private final AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @GetMapping
    public ResponseEntity<List<Airline>> getAirports() {
        List<Airline> airlines = airlineService.getAirlines();
        if (airlines.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(airlines, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Airline> getAirline(@PathVariable Long id) {
        Airline airline = airlineService.getAirline(id).orElseThrow(AirlineNotFoundException::new);
        return new ResponseEntity<>(airline, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createAirline(@Valid @RequestBody Airline airline, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            airlineService.createAirline(airline);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Airline> updateAirport(@Valid @RequestBody Airline airline, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            airlineService.updateAirline(airline);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAirline(@PathVariable Long id) {
        airlineService.deleteAirlineById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
