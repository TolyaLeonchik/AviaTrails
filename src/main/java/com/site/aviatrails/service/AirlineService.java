package com.site.aviatrails.service;

import com.site.aviatrails.domain.Airline;
import com.site.aviatrails.exception.AirportNotFoundException;
import com.site.aviatrails.repository.AirlinesRepository;
import com.site.aviatrails.repository.AirportsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirlineService {

    private final AirlinesRepository airlinesRepository;
    private final AirportsRepository airportsRepository;


    public AirlineService(AirlinesRepository airlinesRepository, AirportsRepository airportsRepository) {
        this.airlinesRepository = airlinesRepository;
        this.airportsRepository = airportsRepository;
    }

    public List<Airline> getAirlines() {
        return airlinesRepository.findAll();
    }

    public Optional<Airline> getAirline(Long id) {
        return airlinesRepository.findById(id);
    }

    public void createAirline(Airline airline) {
        if (airportsRepository.existsById(airline.getAirportId())) {
            airlinesRepository.save(airline);
        } else {
            throw new AirportNotFoundException();
        }
    }

    public void updateAirline(Airline airline) {
        if (airportsRepository.existsById(airline.getAirportId())) {
            airlinesRepository.saveAndFlush(airline);
        } else {
            throw new AirportNotFoundException();
        }
    }

    public void deleteAirlineById(Long id) {
        airlinesRepository.deleteById(id);
    }
}
