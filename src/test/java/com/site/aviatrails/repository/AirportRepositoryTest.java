package com.site.aviatrails.repository;

import com.site.aviatrails.domain.Airport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AirportRepositoryTest {

    @Autowired
    private AirportsRepository airportsRepository;

    static Airport airport;

    @BeforeAll
    static void beforeAll() {
        airport = new Airport();
        airport.setPortName("TestPortName");
        airport.setPortCity("TestCityName");
        airport.setPortCountry("TestCountryName");
        airport.setAirportCode("TST");
    }

    @Test
    void findAllTest() {
        airportsRepository.save(airport);
        List<Airport> newList = airportsRepository.findAll();
        Assertions.assertNotNull(newList);
    }

    @Test
    void findByIdTest() {
        Airport saved = airportsRepository.save(airport);
        Optional<Airport> newUAirport = airportsRepository.findById(saved.getId());
        Assertions.assertTrue(newUAirport.isPresent());
    }

    @Test
    void saveTest() {
        List<Airport> oldList = airportsRepository.findAll();
        airportsRepository.save(airport);
        List<Airport> newList = airportsRepository.findAll();

        Assertions.assertNotEquals(oldList.size(), newList.size());
    }

    @Test
    void updateTest() {
        Airport newAirport = airportsRepository.save(airport);
        newAirport.setPortCity("UPDATED");
        Airport updatedAirport = airportsRepository.saveAndFlush(newAirport);
        Assertions.assertEquals(updatedAirport.getPortCity(), "UPDATED");
    }

    @Test
    void deleteTest() {
        Airport airportSaved = airportsRepository.save(airport);
        airportsRepository.delete(airportSaved);
        Optional<Airport> airportSearch = airportsRepository.findById(airportSaved.getId());
        Assertions.assertFalse(airportSearch.isPresent());
    }

    @Test
    void findIdByPortNameAndPortCityTest() {
        Airport saved = airportsRepository.save(airport);
        Long newAirportId = airportsRepository.findIdByPortNameAndPortCity(saved.getPortName(), saved.getPortCity());
        Assertions.assertEquals(saved.getId(), newAirportId);
    }

    @Test
    void findIdsByPortCity() {
        Airport saved = airportsRepository.save(airport);
        List<Long> searchIds = airportsRepository.findIdsByPortCity(airport.getPortCity());
        Assertions.assertEquals(saved.getId(), searchIds.get(0));
    }
}
