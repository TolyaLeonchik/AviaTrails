package com.site.aviatrails.repository;

import com.site.aviatrails.domain.Flight;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FlightRepositoryTest {

    @Autowired
    private FlightRepository flightRepository;

    @Mock
    Flight flight;

    public void setup() {
        flight = new Flight();
        flight.setAirlineId(1L);
        flight.setFromAirportId(3L);
        flight.setToAirportId(9L);
        flight.setDepartureTime(LocalDateTime.of(2000, 12, 12, 12, 12));
        flight.setArrivalTime(LocalDateTime.of(2000, 12, 12, 14, 12));
        flight.setFlightPrice(500);
        flight.setNumberOfFreeSeats(50);
    }

    @Test
    void findAllTest() {
        setup();
        flightRepository.save(flight);
        List<Flight> newList = flightRepository.findAll();

        Assertions.assertNotNull(newList);
    }

    @Test
    void findByIdTest() {
        setup();
        Flight saved = flightRepository.save(flight);
        Optional<Flight> newFlight = flightRepository.findById(saved.getId());
        Assertions.assertTrue(newFlight.isPresent());
    }

    @Test
    void saveTest() {
        setup();
        List<Flight> oldList = flightRepository.findAll();
        flightRepository.save(flight);
        List<Flight> newList = flightRepository.findAll();
        Assertions.assertNotEquals(oldList.size(), newList.size());
    }

    @Test
    void updateTest() {
        setup();
        Flight newFlight = flightRepository.save(flight);
        newFlight.setFlightPrice(600);
        Flight updatedFlight = flightRepository.saveAndFlush(newFlight);
        Assertions.assertEquals(updatedFlight.getFlightPrice(), 600);
    }

    @Test
    void deleteTest() {
        setup();
        Flight flightSaved = flightRepository.save(flight);
        flightRepository.delete(flightSaved);
        Optional<Flight> airlineSearch = flightRepository.findById(flightSaved.getId());
        Assertions.assertFalse(airlineSearch.isPresent());
    }

    @Test
    void findIdByParametersTest() {
        setup();
        Flight saved = flightRepository.save(flight);
        Long searchId = flightRepository.findIdByParameters(3L, 9L, LocalDateTime.of(2000, 12, 12, 12, 12));
        Assertions.assertEquals(saved.getId(), searchId);
    }

    @Test
    void findNumberOfFreeSeatsByIdTest() {
        setup();
        flightRepository.save(flight);
        Integer search = flightRepository.findNumberOfFreeSeatsById(flight.getId());
        Assertions.assertEquals(50, search);
    }

    @Test
    void findFlightPriceByIdTest() {
        setup();
        flightRepository.save(flight);
        Integer searchId = flightRepository.findFlightPriceById(flight.getId());
        Assertions.assertEquals(500, searchId);
    }

    @Test
    void updateNumberOfFreeSeatsByIdTest() {
        setup();
        Flight newFlight = flightRepository.save(flight);
        newFlight.setNumberOfFreeSeats(49);
        Flight updatedFlight = flightRepository.saveAndFlush(newFlight);
        Assertions.assertEquals(updatedFlight.getNumberOfFreeSeats(), 49);
    }

    @Test
    void findByCityFromAndCityToAndLocalDateTest() {
        setup();
        List<Flight> flightList = new ArrayList<>();
        List<Long> from = new ArrayList<>();
        List<Long> to = new ArrayList<>();
        flightList.add(flight);
        from.add(3L);
        to.add(9L);
        flightRepository.save(flight);
        List<Flight> flightListSearch = flightRepository.findByCityFromAndCityToAndLocalDate(from, to, LocalDate.of(2000, 12, 12));
        Assertions.assertEquals(flightListSearch, flightList);
    }
}
