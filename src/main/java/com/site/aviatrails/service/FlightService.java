package com.site.aviatrails.service;

import com.site.aviatrails.domain.Flight;
import com.site.aviatrails.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }


    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }
}
