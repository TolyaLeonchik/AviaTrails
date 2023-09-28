package com.site.aviatrails.service;

import com.site.aviatrails.domain.*;
import com.site.aviatrails.domain.tickets.UserTicketInfo;
import com.site.aviatrails.repository.AirlinesRepository;
import com.site.aviatrails.repository.AirportsRepository;
import com.site.aviatrails.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
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
        FlightInfo flightInfo = new FlightInfo();
        Optional<Flight> flightById = flightRepository.findById(id);

        if (flightById.isEmpty()) {
            return null; //TODO: EXCEPTION!!!
        }

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

    public List<FlightInfo> findByParameters(String cityOfDeparture, String cityOfArrival, Date date) {
        List<Long> airportsOfDeparture = airportsRepository.findIdByPortCity(cityOfDeparture);
        List<Long> airportsOfArrival = airportsRepository.findIdByPortCity(cityOfArrival);
        List<Long> searchDeparture = new ArrayList<>();
        List<Long> searchArrival = new ArrayList<>();
        List<FlightInfo> flightSearchResult = new ArrayList<>();

        List<Long> flightAll = flightRepository.findAllIds();

        for (Long flightId : flightAll) {
            Optional<Flight> flightSearch = flightRepository.findById(flightId);
            for (Long departure : airportsOfDeparture) {
                if (flightSearch.isPresent() && flightSearch.get().getFromAirportId().equals(departure)) {
                    Long searchIdDeparture = flightSearch.get().getId();
                    searchDeparture.add(searchIdDeparture);
                }
            }
        }

        for (Long flightId : searchDeparture) {
            Optional<Flight> flightSearch = flightRepository.findById(flightId);
            for (Long departure : airportsOfArrival) {
                if (flightSearch.isPresent() && flightSearch.get().getToAirportId().equals(departure)) {
                    Long searchIdDeparture = flightSearch.get().getId();
                    searchArrival.add(searchIdDeparture);
                }
            }
        }

        for (Long flightId : searchArrival) {
            FlightInfo flightInfo = findById(flightId);
            flightSearchResult.add(flightInfo);
        }
        return flightSearchResult;
    }
}
