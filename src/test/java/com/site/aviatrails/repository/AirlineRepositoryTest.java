package com.site.aviatrails.repository;

import com.site.aviatrails.domain.Airline;
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
public class AirlineRepositoryTest {

    @Autowired
    private AirlinesRepository airlinesRepository;

    static Airline airline;

    @BeforeAll
    static void beforeAll() {
        airline = new Airline();
        airline.setAirlineName("TestName");
        airline.setAirlineCountry("TestCountryName");
        airline.setAirportId(5L);
    }

    @Test
    void findAllTest() {
        airlinesRepository.save(airline);
        List<Airline> newList = airlinesRepository.findAll();
        Assertions.assertNotNull(newList);
    }

    @Test
    void findByIdTest() {
        Airline saved = airlinesRepository.save(airline);
        Optional<Airline> newAirline = airlinesRepository.findById(saved.getId());
        Assertions.assertTrue(newAirline.isPresent());
    }

    @Test
    void saveTest() {
        List<Airline> oldList = airlinesRepository.findAll();
        airlinesRepository.save(airline);
        List<Airline> newList = airlinesRepository.findAll();
        Assertions.assertNotEquals(oldList.size(), newList.size());
    }

    @Test
    void updateTest() {
        Airline newAirline = airlinesRepository.save(airline);
        newAirline.setAirlineName("UPDATED");
        Airline updatedAirline = airlinesRepository.saveAndFlush(newAirline);
        Assertions.assertEquals(updatedAirline.getAirlineName(), "UPDATED");
    }

    @Test
    void deleteTest() {
        Airline airlineSaved = airlinesRepository.save(airline);
        airlinesRepository.delete(airlineSaved);
        Optional<Airline> airlineSearch = airlinesRepository.findById(airlineSaved.getId());
        Assertions.assertFalse(airlineSearch.isPresent());
    }
}
