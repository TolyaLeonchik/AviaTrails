package com.site.aviatrails.service;

import com.site.aviatrails.domain.*;
import com.site.aviatrails.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AirportsRepository airportsRepository;
    private final AirlinesRepository airlinesRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final Airline airline;
    private final Airport airport;
    private final Flight flight;
    private final UserInfo userInfo;

    public ReservationService(ReservationRepository reservationRepository, AirportsRepository airportsRepository,
                              AirlinesRepository airlinesRepository, FlightRepository flightRepository,
                              UserRepository userRepository, Airline airline, Airport airport, Flight flight, UserInfo userInfo) {
        this.reservationRepository = reservationRepository;
        this.airportsRepository = airportsRepository;
        this.airlinesRepository = airlinesRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
        this.airline = airline;
        this.airport = airport;
        this.flight = flight;
        this.userInfo = userInfo;
    }

    public List<Ticket> getAllTickets() {
        return reservationRepository.findAll();
    }

    public Integer availableSeats(Integer flightId) {
        
    }

    public void bookingTicket(BookingTicketDTO bookingTicketDTO) {
        //Нужно правильно раздать всем бд информацию
        //TODO:
        //Проверка есть ли места для этого рейса
        //1. flight id достать методом поиска ид по 2 аэропортов
        //1. user id достать методом поиска ид
        //1. price скорее всего в таблицу travel
        boolean hasEmptySeat;
        Ticket ticket = new Ticket();
        Long fromId = airportsRepository.findIdByPortName(bookingTicketDTO.getPortNameFrom());
        Long toId = airportsRepository.findIdByPortName(bookingTicketDTO.getPortNameTo());
        //TODO: я вставлял в лонг строку!!!!!! переделать
        Long flightId = flightRepository
                .findIdByFromAirportIdAndToAirportId(airportsRepository.findIdByPortName(bookingTicketDTO.getPortNameFrom()),
                        airportsRepository.findIdByPortName(bookingTicketDTO.getPortNameTo()));
        ticket.setFlightId(flightId);
        ticket.setPassengerId(userRepository
                .findIdByFirstNameAndLastName(bookingTicketDTO.getFirstName(), bookingTicketDTO.getLastName()));
        reservationRepository.save(ticket);
    }
}
