package com.site.aviatrails.service;
//TODO:Все звёзды переделать
import com.site.aviatrails.domain.*;
import com.site.aviatrails.domain.tickets.BookingTicketDTO;
import com.site.aviatrails.domain.tickets.Ticket;
import com.site.aviatrails.domain.tickets.UserTicketInfo;
import com.site.aviatrails.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final AirportsRepository airportsRepository;
    private final AirlinesRepository airlinesRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, AirportsRepository airportsRepository,
                         AirlinesRepository airlinesRepository, FlightRepository flightRepository,
                         UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.airportsRepository = airportsRepository;
        this.airlinesRepository = airlinesRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    //TODO: нужно ли админам полная как в этом методе информация об всех забронированных билетах?
    //TODO: нужно ли тоже сделать поиски через Optional?
    public List<UserTicketInfo> getUserTicketsInfo(Long id) {
        List<Long> ticketsIds = ticketRepository.findIdsByPassengerId(id);
        List<UserTicketInfo> userTicketsInfo = new ArrayList<>();
        Optional<UserInfo> userInfo = userRepository.findById(id);

        for (Long ticketId : ticketsIds) {
            UserTicketInfo userTicketInfo = new UserTicketInfo();
            Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
            Optional<Flight> flight = Optional.empty();
            //TODO: как лучше сделать проверку
            if (ticketOptional.isPresent()) {
                flight = flightRepository.findById(ticketOptional.get().getFlightId());
            }

            //TODO: лучше через @Query или через Optional.get().getExample()?
            /** Optional*/
            long flightId = ticketRepository.findFlightIdById(ticketId);
            long airlineId = flightRepository.findAirlineById(flightId);
            long fromId = flightRepository.findFromAirportIdById(flightId);
            long toId = flightRepository.findToAirportIdById(flightId);

            if (userInfo.isPresent() && flight.isPresent()) {
                userTicketInfo.setFirstName(userInfo.get().getFirstName());
                userTicketInfo.setLastName(userInfo.get().getLastName());
                userTicketInfo.setAirlineName(airlinesRepository.findAirlineNameById(airlineId));

                //TODO: подправить информацию о городе выезда/приезда, возможно и порты добавить
                //TODO: обраный билет + багаж (булеан + к цене за билет
                userTicketInfo.setPortCityFrom(airportsRepository.findPortNameById(fromId));
                userTicketInfo.setPortCityTo(airportsRepository.findPortNameById(toId));

                userTicketInfo.setDepartureTime(flight.get().getDepartureTime());
                userTicketInfo.setArrivalTime(flight.get().getArrivalTime());
                userTicketInfo.setNumberOfTickets(ticketOptional.get().getNumberOfTickets());
                userTicketInfo.setSeatNumber(ticketOptional.get().getSeatNumber());
                userTicketInfo.setTicketPrice(ticketOptional.get().getTicketPrice());

                userTicketsInfo.add(userTicketInfo);
            }
        }
        return userTicketsInfo;
    }

    //TODO: нужно ли вызывать отдельно этот метод?
    public boolean checkAvailableSeats(Long flightId, Integer bookedSeats) {
        int totalSeats = flightRepository.findNumberOfFreeSeatsById(flightId);
        int availableSeats = totalSeats - bookedSeats;
        return availableSeats >= 0;
    }

    @Transactional(rollbackFor = Exception.class)   //откатка
    public void bookingTicket(BookingTicketDTO bookingTicketDTO) {
        //Нужно правильно раздать всем бд информацию
        //TODO:
        /** DONE*///CКОЛЬКО БИЛЕТОВ ПОКУПАЮТ
        /** DONE*///Проверка есть ли места для этого рейса
        //Проверка есть ли такой юзер наверное чтобы null не передавашка


        Ticket ticket = new Ticket();

        Long flightId = flightRepository.
                findIdByFromAirportIdAndToAirportId(airportsRepository.
                                findIdByPortNameAndPortCity(bookingTicketDTO.getPortNameFrom(), bookingTicketDTO.getPortCityFrom()),
                        airportsRepository.findIdByPortNameAndPortCity(bookingTicketDTO.getPortNameTo(), bookingTicketDTO.getPortCityTo()));
        if (!checkAvailableSeats(flightId, bookingTicketDTO.getCountOfTickets())) {
            System.out.println("No free seats!");
            return;
        }

        ticket.setFlightId(flightId);
        ticket.setPassengerId(userRepository
                .findIdByFirstNameAndLastName(bookingTicketDTO.getFirstName(), bookingTicketDTO.getLastName()));
        ticket.setTicketPrice(flightRepository.findFlightPriceById(flightId) * bookingTicketDTO.getCountOfTickets());
        ticket.setSeatNumber(flightRepository.findNumberOfFreeSeatsById(flightId));
        ticket.setNumberOfTickets(bookingTicketDTO.getCountOfTickets());

        flightRepository
                .updateNumberOfFreeSeatsById(ticket.getSeatNumber() - bookingTicketDTO.getCountOfTickets(), flightId);
        ticketRepository.save(ticket);
    }

    public void refundTicket(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        Optional<Flight> flight = flightRepository.findById(ticket.get().getFlightId());
        flight.get().setNumberOfFreeSeats(flight.get().getNumberOfFreeSeats() + ticket.get().getNumberOfTickets());

        //TODO:      нужна ли логика для списывания/возврата денег на карту?  Ticket refundMoney(ticket.get().getTicketPrice());
        if (ticket.isPresent() && flight.isPresent()) {
            ticketRepository.delete(ticket.get());
            flightRepository.saveAndFlush(flight.get());
        }
    }
}
