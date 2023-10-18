package com.site.aviatrails.controller;

import com.site.aviatrails.domain.Flight;
import com.site.aviatrails.domain.FlightInfo;
import com.site.aviatrails.exception.FlightNotFoundException;
import com.site.aviatrails.service.FlightService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/allPage")
    public ResponseEntity<Page<FlightInfo>> getAllFlightsPage(@PageableDefault(
            sort = {"id"}
    ) Pageable pageable, @RequestParam(value = "direction", required = false) String direction,
                                                              @RequestParam(value = "property", required = false) String property) {
        Page<FlightInfo> page = flightService.getFlightsPagesBySort(pageable, direction, property);
        if (page.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(page, HttpStatus.OK);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<FlightInfo>> getFlightByParameters(@PageableDefault(
            sort = {"flightPrice"},
            direction = Sort.Direction.ASC
    ) Pageable pageable, @RequestParam(value = "cityOfDeparture") String cityOfDeparture,
                                                                  @RequestParam(value = "cityOfArrival") String cityOfArrival,
                                                                  @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                  LocalDate date, @RequestParam(value = "direction", required = false) String direction,
                                                                  @RequestParam(value = "property", required = false) String property) {
        Page<FlightInfo> flightInfoSearch = flightService.findByParameters(pageable, cityOfDeparture, cityOfArrival, date,
                direction, property);
        if (flightInfoSearch.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(flightInfoSearch, HttpStatus.OK);
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
