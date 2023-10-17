package com.site.aviatrails.service;

import com.site.aviatrails.domain.Airport;
import com.site.aviatrails.repository.AirportsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirportService {

    private final AirportsRepository airportsRepository;

    public AirportService(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }

    public List<Airport> getAirports() {
        return airportsRepository.findAll();
    }

    public Optional<Airport> getAirport(Long id) {
        return airportsRepository.findById(id);
    }

    public void createAirport(Airport airport) {
        airportsRepository.save(airport);
    }

    public void updateAirport(Airport airport) {
        airportsRepository.saveAndFlush(airport);
    }

    public void deleteAirportById(Long id) {
        airportsRepository.deleteById(id);
    }
}
