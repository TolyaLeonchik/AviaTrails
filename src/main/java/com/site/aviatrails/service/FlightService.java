package com.site.aviatrails.service;

import com.site.aviatrails.domain.Airline;
import com.site.aviatrails.domain.Airport;
import com.site.aviatrails.domain.Flight;
import com.site.aviatrails.domain.FlightInfo;
import com.site.aviatrails.exception.FlightNotFoundException;
import com.site.aviatrails.repository.AirlinesRepository;
import com.site.aviatrails.repository.AirportsRepository;
import com.site.aviatrails.repository.FlightRepository;
import com.site.aviatrails.validator.FlightValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final AirlinesRepository airlinesRepository;
    private final AirportsRepository airportsRepository;
    private final FlightValidator flightValidator;

    public FlightService(FlightRepository flightRepository, AirlinesRepository airlinesRepository,
                         AirportsRepository airportsRepository, FlightValidator flightValidator) {
        this.flightRepository = flightRepository;
        this.airlinesRepository = airlinesRepository;
        this.airportsRepository = airportsRepository;
        this.flightValidator = flightValidator;
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Optional<Flight> findById(Long id) {
        return flightRepository.findById(id);
    }

    public FlightInfo findInfoById(Long id) {
        Optional<Flight> flightById = flightRepository.findById(id);

        if (flightById.isEmpty()) {
            throw new FlightNotFoundException();
        }
        return mapToDto(flightById.get());
    }

    public Page<FlightInfo> findByParameters(Pageable pageable, String cityOfDeparture, String cityOfArrival, LocalDate date,
                                             String direction, String property) {
        List<Long> airportsOfDeparture = airportsRepository.findIdsByPortCity(cityOfDeparture);
        List<Long> airportsOfArrival = airportsRepository.findIdsByPortCity(cityOfArrival);
        List<Flight> flightSearchByParameters = flightRepository.findByCityFromAndCityToAndLocalDate(airportsOfDeparture, airportsOfArrival, date);

        if (flightSearchByParameters.isEmpty()) {
            throw new FlightNotFoundException();
        }
        if (checkList(direction, property) && property.equals("flightPrice")) {
            sortByPrice(flightSearchByParameters, direction);
            Sort.Direction sortDirection = Sort.Direction.fromString(direction);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sortDirection, property));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC, "id");
        }

        Page<Flight> flights = new PageImpl<>(flightSearchByParameters, pageable, flightSearchByParameters.size());
        return flights.map(this::mapToDto);
    }

    private List<Flight> sortByPrice(List<Flight> flights, String direction) {
        flights.sort(Comparator.comparing(Flight::getFlightPrice));

        if ("desc".equalsIgnoreCase(direction)) {
            Collections.reverse(flights);
        }
        return flights;
    }

    public void createFlight(Flight flight) {
        flightValidator.validateFlight(flight);
        flightRepository.save(flight);
    }

    public void updateFlight(Flight flight) {
        flightValidator.validateFlight(flight);
        flightRepository.saveAndFlush(flight);
    }

    public void deleteFlightById(Long id) {
        flightRepository.deleteById(id);
    }

    public Page<FlightInfo> getFlightsPagesBySort(Pageable pageable, String direction, String property) {
        if (checkList(direction, property)) {
            Sort.Direction sortDirection = Sort.Direction.fromString(direction);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sortDirection, property));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC, "id");
        }

        Page<Flight> flights = flightRepository.findAll(pageable);
        return flights.map(this::mapToDto);
    }

    private boolean checkList(String direction, String property) {
        return direction != null && !direction.isEmpty() && (direction.equals("desc") || direction.equals("asc"))
                && property != null && !property.isEmpty();
    }

    private FlightInfo mapToDto(Flight flightById) {
        Optional<Airline> airline = airlinesRepository.findById(flightById.getAirlineId());
        Optional<Airport> airportFrom = airportsRepository.findById(flightById.getFromAirportId());
        Optional<Airport> airportTo = airportsRepository.findById(flightById.getToAirportId());

        FlightInfo flightInfo = new FlightInfo();
        if (airline.isPresent() && airportFrom.isPresent() && airportTo.isPresent()) {

            flightInfo.setAirline(airline.get().getAirlineName());
            flightInfo.setPortCityFrom(airportFrom.get().getPortCity());
            flightInfo.setPortCityTo(airportTo.get().getPortCity());
            flightInfo.setPortNameFrom(airportFrom.get().getPortName());
            flightInfo.setPortNameTo(airportTo.get().getPortName());
            flightInfo.setAirportCodeFrom(airportFrom.get().getAirportCode());
            flightInfo.setAirportCodeTo(airportTo.get().getAirportCode());
            flightInfo.setDepartureTime(flightById.getDepartureTime());
            flightInfo.setArrivalTime(flightById.getArrivalTime());
            flightInfo.setFlightPrice(flightById.getFlightPrice());
            flightInfo.setNumberOfFreeSeats(flightById.getNumberOfFreeSeats());

            Duration duration = Duration.between(flightInfo.getDepartureTime(), flightInfo.getArrivalTime());
            long seconds = duration.getSeconds();
            flightInfo.setFlightDurationInHours(seconds / 3600.0);
        }
        return flightInfo;
    }
}
