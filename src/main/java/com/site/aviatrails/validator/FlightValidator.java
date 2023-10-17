package com.site.aviatrails.validator;

import com.site.aviatrails.domain.Flight;
import com.site.aviatrails.exception.AirlineNotFoundException;
import com.site.aviatrails.exception.AirportNotFoundException;
import com.site.aviatrails.exception.BadRequestException;
import com.site.aviatrails.repository.AirlinesRepository;
import com.site.aviatrails.repository.AirportsRepository;
import org.springframework.stereotype.Component;

@Component
public class FlightValidator {

    private final AirlinesRepository airlinesRepository;
    private final AirportsRepository airportsRepository;

    public FlightValidator(AirlinesRepository airlinesRepository, AirportsRepository airportsRepository) {
        this.airlinesRepository = airlinesRepository;
        this.airportsRepository = airportsRepository;
    }

    public void validateFlight(Flight flight) {
        if (!airlinesRepository.existsById(flight.getAirlineId())) {
            throw new AirlineNotFoundException();
        }
        if (!airportsRepository.existsById(flight.getFromAirportId()) || !airportsRepository.existsById(flight.getToAirportId())) {
            throw new AirportNotFoundException();
        }
        if (flight.getFromAirportId().equals(flight.getToAirportId()) || flight.getDepartureTime().isAfter(flight.getArrivalTime())) {
            throw new BadRequestException();
        }
    }
}
