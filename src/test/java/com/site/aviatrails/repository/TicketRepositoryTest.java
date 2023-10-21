package com.site.aviatrails.repository;

import com.site.aviatrails.domain.Airline;
import com.site.aviatrails.domain.Airport;
import com.site.aviatrails.domain.Flight;
import com.site.aviatrails.domain.UserInfo;
import com.site.aviatrails.domain.tickets.Ticket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Mock
    Ticket ticket;

    @Mock
    private AirportsRepository airportsRepository;

    @Mock
    private AirlinesRepository airlinesRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private UserInfo userInfo;

    public void setup() {
        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setFlightId(1L);
        ticket.setPassengerId(3L);
        ticket.setActiveStatus(true);
        ticket.setNumberOfTickets(1);
        ticket.setTicketPrice(500);
        ticket.setSeatNumber(14);

        userInfo.setId(3L);
        userRepository.save(userInfo);
    }

    @Test
    void findAllTest() {
        setup();
        ticketRepository.save(ticket);
        List<Ticket> newList = ticketRepository.findAll();

        assertNotNull(newList);
    }

    @Test
    void findByIdTest() {
        setup();
        Ticket saved = ticketRepository.save(ticket);
        Optional<Ticket> newTicket = ticketRepository.findById(saved.getId());
        Assertions.assertTrue(newTicket.isPresent());
    }

    @Test
    void saveTest() {
        setup();
        List<Ticket> oldList = ticketRepository.findAll();
        ticketRepository.save(ticket);
        List<Ticket> newList = ticketRepository.findAll();
        Assertions.assertNotEquals(oldList.size(), newList.size());
    }

    @Test
    void deleteTest() {
        setup();
        Ticket ticketSaved = ticketRepository.save(ticket);
        ticketRepository.delete(ticketSaved);
        Optional<Ticket> ticketSearch = ticketRepository.findById(ticketSaved.getId());
        Assertions.assertFalse(ticketSearch.isPresent());
    }

    @Test
    void findByPassengerIdTest() {
        setup();
        Ticket saved = ticketRepository.save(ticket);
        List<Ticket> newTicket = ticketRepository.findByPassengerId(saved.getPassengerId());
        Assertions.assertFalse(newTicket.isEmpty());
    }

    @Test
    void findIdsByPassengerIdTest() {
        setup();
        Ticket saved = ticketRepository.save(ticket);
        List<Long> newTicket = ticketRepository.findIdsByPassengerId(saved.getPassengerId());
        assertEquals(newTicket.get(0), saved.getId());
    }

    @Test
    void getTicketWithDetailsTest() {
        setup();
        Long userId = 3L;
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAirlineId(2L);
        Airline airline = new Airline();
        airline.setId(2L);
        airline.setAirportId(3L);
        Airport airportFrom = new Airport();
        airportFrom.setId(4L);
        Airport airportTo = new Airport();
        airportTo.setId(5L);

        flightRepository.save(flight);
        airlinesRepository.save(airline);
        airportsRepository.save(airportFrom);
        airportsRepository.save(airportTo);

        Ticket search = new Ticket();
        search.setFlightId(1L);
        search.setPassengerId(userId);
        search.setSeatNumber(12);
        search.setTicketPrice(222);
        search.setActiveStatus(true);
        search.setNumberOfTickets(1);
        ticketRepository.save(search);

        when(airportsRepository.findById(3L)).thenReturn(Optional.of(airportFrom));
        when(airportsRepository.findById(4L)).thenReturn(Optional.of(airportTo));
        when(airlinesRepository.findById(2L)).thenReturn(Optional.of(airline));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        List<Object[]> results = ticketRepository.getTicketWithDetails(3L);

        Object[] result = results.get(0);

        assertNotNull(result);
        assertEquals(1, results.size());
    }
}
