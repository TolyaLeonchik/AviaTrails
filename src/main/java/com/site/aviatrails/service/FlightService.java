package com.site.aviatrails.service;

import com.site.aviatrails.domain.Airline;
import com.site.aviatrails.domain.Airport;
import com.site.aviatrails.domain.Flight;
import com.site.aviatrails.domain.FlightInfo;
import com.site.aviatrails.exception.FlightNotFoundException;
import com.site.aviatrails.repository.AirlinesRepository;
import com.site.aviatrails.repository.AirportsRepository;
import com.site.aviatrails.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final AirlinesRepository airlinesRepository;
    private final AirportsRepository airportsRepository;

    public FlightService(FlightRepository flightRepository, AirlinesRepository airlinesRepository, AirportsRepository airportsRepository) {
        this.flightRepository = flightRepository;
        this.airlinesRepository = airlinesRepository;
        this.airportsRepository = airportsRepository;
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public FlightInfo findById(Long id) {
        Optional<Flight> flightById = flightRepository.findById(id);

        if (flightById.isEmpty()) {
            throw new FlightNotFoundException();
        }

        FlightInfo flightInfo = new FlightInfo();

        Optional<Airline> airline = airlinesRepository.findById(flightById.get().getAirlineId());
        Optional<Airport> airportFrom = airportsRepository.findById(flightById.get().getFromAirportId());
        Optional<Airport> airportTo = airportsRepository.findById(flightById.get().getToAirportId());

        if (airline.isPresent() && airportFrom.isPresent() && airportTo.isPresent()) {

            flightInfo.setAirline(airline.get().getAirlineName());
            flightInfo.setPortCityFrom(airportFrom.get().getPortCity());
            flightInfo.setPortCityTo(airportTo.get().getPortCity());
            flightInfo.setPortNameFrom(airportFrom.get().getPortName());
            flightInfo.setPortNameTo(airportTo.get().getPortName());
            flightInfo.setAirportCodeFrom(airportFrom.get().getAirportCode());
            flightInfo.setAirportCodeTo(airportTo.get().getAirportCode());
            flightInfo.setDepartureTime(flightById.get().getDepartureTime());
            flightInfo.setArrivalTime(flightById.get().getArrivalTime());
            flightInfo.setFlightPrice(flightById.get().getFlightPrice());
            flightInfo.setNumberOfFreeSeats(flightById.get().getNumberOfFreeSeats());

            Duration duration = Duration.between(flightInfo.getDepartureTime(), flightInfo.getArrivalTime());
            long seconds = duration.getSeconds();
            flightInfo.setFlightDurationInHours(seconds / 3600.0);
        }
        return flightInfo;
    }

    public List<FlightInfo> findByParameters(String cityOfDeparture, String cityOfArrival, LocalDate date) {
        List<Long> airportsOfDeparture = airportsRepository.findIdsByPortCity(cityOfDeparture);
        List<Long> airportsOfArrival = airportsRepository.findIdsByPortCity(cityOfArrival);
        List<Long> flightSearchByParameters = flightRepository.findIdsByCityFromAndCityToAndLocalDate(airportsOfDeparture, airportsOfArrival, date);

        if (flightSearchByParameters.isEmpty()) {
            throw new FlightNotFoundException();
        }

        List<FlightInfo> flightSearchResult = new ArrayList<>();

        for (Long flightId : flightSearchByParameters) {
            FlightInfo flightInfo = findById(flightId);
            flightSearchResult.add(flightInfo);
        }
        return flightSearchResult;
    }
}
