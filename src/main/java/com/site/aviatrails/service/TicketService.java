package com.site.aviatrails.service;

import com.site.aviatrails.domain.CardInfo;
import com.site.aviatrails.domain.Flight;
import com.site.aviatrails.domain.PaymentDTO;
import com.site.aviatrails.domain.UserInfo;
import com.site.aviatrails.domain.tickets.BookingTicketDTO;
import com.site.aviatrails.domain.tickets.Ticket;
import com.site.aviatrails.domain.tickets.UserTicketInfo;

import com.site.aviatrails.exception.BookingTimeExpiredException;
import com.site.aviatrails.exception.InsufficientFunds;
import com.site.aviatrails.exception.NoTicketsFoundException;
import com.site.aviatrails.exception.TicketAlreadyPaidException;

import com.site.aviatrails.repository.AirlinesRepository;
import com.site.aviatrails.repository.AirportsRepository;
import com.site.aviatrails.repository.FlightRepository;
import com.site.aviatrails.repository.PaymentHistoryRepository;
import com.site.aviatrails.repository.TicketRepository;
import com.site.aviatrails.repository.UserRepository;
import com.site.aviatrails.validator.BookingValidator;
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
    private final PaymentHistoryRepository paymentHistoryRepository;

    private static final long BOOKING_TIME_LIMIT_MS = 15 * 60 * 1000;

    public TicketService(TicketRepository ticketRepository, AirportsRepository airportsRepository,
                         AirlinesRepository airlinesRepository, FlightRepository flightRepository,
                         UserRepository userRepository, PaymentHistoryRepository paymentHistoryRepository) {
        this.ticketRepository = ticketRepository;
        this.airportsRepository = airportsRepository;
        this.airlinesRepository = airlinesRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
        this.paymentHistoryRepository = paymentHistoryRepository;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public List<UserTicketInfo> getUserTicketsInfo(Long id) {
        BookingValidator bookingValidator = new BookingValidator();
        bookingValidator.validateTicketExistenceByPassengerId(ticketRepository.findIdsByPassengerId(id));
        bookingValidator.validateUserExistence(id);

        List<Long> ticketsIds = ticketRepository.findIdsByPassengerId(id);
        List<UserTicketInfo> userTicketsInfo = new ArrayList<>();
        Optional<UserInfo> userInfo = userRepository.findById(id);

        for (Long ticketId : ticketsIds) {
            UserTicketInfo userTicketInfo = new UserTicketInfo();
            Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
            Optional<Flight> flight = Optional.empty();

            if (ticketOptional.isPresent()) {
                flight = flightRepository.findById(ticketOptional.get().getFlightId());
            }

            long flightId = ticketRepository.findFlightIdById(ticketId);
            long airlineId = flightRepository.findAirlineById(flightId);
            long fromId = flightRepository.findFromAirportIdById(flightId);
            long toId = flightRepository.findToAirportIdById(flightId);

            if (userInfo.isPresent() && flight.isPresent()) {
                userTicketInfo.setFirstName(userInfo.get().getFirstName());
                userTicketInfo.setLastName(userInfo.get().getLastName());
                userTicketInfo.setAirlineName(airlinesRepository.findAirlineNameById(airlineId));

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

    @Transactional(rollbackFor = Exception.class)
    public void bookingTicket(BookingTicketDTO bookingTicketDTO) {

        long bookingStartTime = System.currentTimeMillis();

        Long flightId = flightRepository.
                findIdByParameters(airportsRepository.
                                findIdByPortNameAndPortCity(bookingTicketDTO.getPortNameFrom(), bookingTicketDTO.getPortCityFrom()),
                        airportsRepository.findIdByPortNameAndPortCity(bookingTicketDTO.getPortNameTo(), bookingTicketDTO.getPortCityTo()),
                        bookingTicketDTO.getDepartureTime());
        Long flightReturningId = null;

        BookingValidator bookingValidator = new BookingValidator();
        bookingValidator.validateFlightExistence(flightId);
        bookingValidator.validateUserExistence(userRepository
                .findIdByFirstNameAndLastName(bookingTicketDTO.getFirstName(), bookingTicketDTO.getLastName()));
        bookingValidator.validateSeatAvailability(flightRepository.findNumberOfFreeSeatsById(flightId), bookingTicketDTO.getCountOfTickets());

        if (bookingTicketDTO.isReturnTicket()) {
            flightReturningId = flightRepository.
                    findIdByParameters(airportsRepository.
                                    findIdByPortNameAndPortCity(bookingTicketDTO.getReturnPortNameFrom(), bookingTicketDTO.getReturnPortCityFrom()),
                            airportsRepository.findIdByPortNameAndPortCity(bookingTicketDTO.getReturnPortNameTo(), bookingTicketDTO.getReturnPortCityTo()),
                            bookingTicketDTO.getReturnDepartureTime());
            bookingValidator.validateFlightExistence(flightReturningId);
            bookingValidator.validateSeatAvailability(flightRepository.findNumberOfFreeSeatsById(flightReturningId), bookingTicketDTO.getReturnCountOfTickets());
        }

        Ticket ticket = new Ticket();
        Ticket returningTicket = bookingTicketDTO.isReturnTicket() ? new Ticket() : null;

        ticketFactory(ticket, returningTicket, flightId, flightReturningId, bookingTicketDTO);

        if (bookingValidator.validateBookingTime(bookingStartTime, BOOKING_TIME_LIMIT_MS)) {
            throw new BookingTimeExpiredException();
        }

        ticketRepository.save(ticket);

        if (bookingTicketDTO.isReturnTicket() && returningTicket != null) {
            ticketRepository.save(returningTicket);
        }
    }

    private void ticketFactory(Ticket ticket, Ticket returningTicket, Long flightId, Long flightReturningId, BookingTicketDTO bookingTicketDTO) {
        ticket.setFlightId(flightId);
        ticket.setPassengerId(userRepository
                .findIdByFirstNameAndLastName(bookingTicketDTO.getFirstName(), bookingTicketDTO.getLastName()));
        ticket.setTicketPrice(flightRepository.findFlightPriceById(flightId) * bookingTicketDTO.getCountOfTickets());
        ticket.setSeatNumber(flightRepository.findNumberOfFreeSeatsById(flightId));
        ticket.setNumberOfTickets(bookingTicketDTO.getCountOfTickets());

        flightRepository
                .updateNumberOfFreeSeatsById(ticket.getSeatNumber() - bookingTicketDTO.getCountOfTickets(), flightId);

        if (bookingTicketDTO.isReturnTicket()) {

            BookingValidator bookingValidator = new BookingValidator();
            bookingValidator.validateFlightExistence(flightReturningId);
            bookingValidator.validateSeatAvailability(flightRepository.findNumberOfFreeSeatsById(flightReturningId), bookingTicketDTO.getReturnCountOfTickets());

            returningTicket.setFlightId(flightReturningId);
            returningTicket.setPassengerId(ticket.getPassengerId());
            returningTicket.setTicketPrice(flightRepository.findFlightPriceById(flightReturningId) * bookingTicketDTO.getReturnCountOfTickets());
            returningTicket.setSeatNumber(flightRepository.findNumberOfFreeSeatsById(flightReturningId));
            returningTicket.setNumberOfTickets(bookingTicketDTO.getReturnCountOfTickets());

            flightRepository
                    .updateNumberOfFreeSeatsById(returningTicket.getSeatNumber() - bookingTicketDTO.getReturnCountOfTickets(), flightReturningId);

        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void payment(Long passengerId, CardInfo cardInfo) throws InsufficientFunds {
        List<Ticket> unpaidTickets = getUnpaidTickets(passengerId);

        int price = calculatePrice(unpaidTickets);

        if (cardInfo.getBalance() < price) {
            throw new InsufficientFunds();
        }

        for (Ticket ticket : unpaidTickets) {
            PaymentDTO paymentDTO = createPayment(ticket, cardInfo);
            paymentHistoryRepository.save(paymentDTO);
        }
        cardInfo.setBalance(cardInfo.getBalance() - price);
    }

    private int calculatePrice(List<Ticket> unpaidTickets) {
        if (unpaidTickets.isEmpty()) {
            throw new NoTicketsFoundException();
        }

        int price = 0;

        for (Ticket ticket : unpaidTickets) {
            price = price + ticket.getTicketPrice();
        }
        return price;
    }

    private PaymentDTO createPayment(Ticket ticket, CardInfo cardInfo) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setTicketId(ticket.getId());
        paymentDTO.setPassengerId(ticket.getPassengerId());
        paymentDTO.setCardNumber(cardInfo.getNumberCard());

        return paymentDTO;
    }

    private List<Ticket> getUnpaidTickets(Long passengerId) {
        List<Ticket> passengerTickets = ticketRepository.findByPassengerId(passengerId);
        List<Ticket> unpaidTickets = new ArrayList<>();

        for (Ticket ticket : passengerTickets) {
            if (!paymentHistoryRepository.existsByTicketId(ticket.getId())) {
                unpaidTickets.add(ticket);
            }
        }

        if (unpaidTickets.isEmpty()) {
            throw new TicketAlreadyPaidException();
        }
        return unpaidTickets;
    }

    @Transactional(rollbackFor = Exception.class)
    public void refundTicket(Long id) {
        BookingValidator bookingValidator = new BookingValidator();
        bookingValidator.validateTicketExistence(ticketRepository.existsById(id));
        bookingValidator.validatePaymentExistence(paymentHistoryRepository.existsByTicketId(id));

        Optional<Ticket> ticket = ticketRepository.findById(id);
        Optional<PaymentDTO> paymentDTO = paymentHistoryRepository.findByTicketId(id);

        flightRefund(ticket);
        refundPayment(ticket, paymentDTO);
        ticketRepository.delete(ticket.get());
    }

    private void refundPayment(Optional<Ticket> ticket, Optional<PaymentDTO> paymentDTO) {
        CardInfo cardInfo = new CardInfo();
        cardInfo.setNumberCard(paymentDTO.get().getCardNumber());
        cardInfo.setBalance(cardInfo.getBalance() + ticket.get().getTicketPrice());
    }

    private void flightRefund(Optional<Ticket> ticket) {
        Optional<Flight> flight = flightRepository.findById(ticket.get().getFlightId());
        flight.get().setNumberOfFreeSeats(flight.get().getNumberOfFreeSeats() + ticket.get().getNumberOfTickets());
        flightRepository.saveAndFlush(flight.get());
    }
}
