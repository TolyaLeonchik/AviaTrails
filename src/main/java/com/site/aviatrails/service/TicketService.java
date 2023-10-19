package com.site.aviatrails.service;

import com.site.aviatrails.domain.Airline;
import com.site.aviatrails.domain.Airport;
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

import com.site.aviatrails.repository.AirportsRepository;
import com.site.aviatrails.repository.FlightRepository;
import com.site.aviatrails.repository.PaymentHistoryRepository;
import com.site.aviatrails.repository.TicketRepository;
import com.site.aviatrails.repository.UserRepository;
import com.site.aviatrails.validator.BookingValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final AirportsRepository airportsRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final BookingValidator bookingValidator;
    private final CardInfo cardInfo;

    private static final long BOOKING_TIME_LIMIT_MS = 15 * 60 * 1000;

    public TicketService(TicketRepository ticketRepository, AirportsRepository airportsRepository,
                         FlightRepository flightRepository, UserRepository userRepository, CardInfo cardInfo,
                         PaymentHistoryRepository paymentHistoryRepository, BookingValidator bookingValidator) {
        this.ticketRepository = ticketRepository;
        this.airportsRepository = airportsRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
        this.paymentHistoryRepository = paymentHistoryRepository;
        this.bookingValidator = bookingValidator;
        this.cardInfo = cardInfo;
    }

    public Page<Ticket> getAllTickets(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC, "id");
        return ticketRepository.findAll(pageable);
    }

    public Page<UserTicketInfo> getUserTicketsInfoById(Long id, Pageable pageable) {

        bookingValidator.validateTicketExistenceByPassengerId(ticketRepository.findIdsByPassengerId(id));
        bookingValidator.validateUserExistence(id);

        List<UserTicketInfo> userTicketsInfo = new ArrayList<>();
        Optional<UserInfo> userInfo = userRepository.findById(id);
        List<Object[]> ticketWithDetails = ticketRepository.getTicketWithDetails(id);

        for (Object[] ticketFromDb : ticketWithDetails) {
            UserTicketInfo userTicketInfo = new UserTicketInfo();

            Ticket ticketDb = (Ticket) ticketFromDb[0];
            Flight flightDb = (Flight) ticketFromDb[1];
            Airline airlineDb = (Airline) ticketFromDb[2];
            Airport airportFrom = (Airport) ticketFromDb[3];
            Airport airportTo = (Airport) ticketFromDb[4];

            userTicketInfo.setFirstName(userInfo.get().getFirstName());
            userTicketInfo.setLastName(userInfo.get().getLastName());
            userTicketInfo.setAirlineName(airlineDb.getAirlineName());

            userTicketInfo.setPortCityFrom(airportFrom.getPortCity());
            userTicketInfo.setPortCityTo(airportTo.getPortCity());

            userTicketInfo.setDepartureTime(flightDb.getDepartureTime());
            userTicketInfo.setArrivalTime(flightDb.getArrivalTime());
            userTicketInfo.setNumberOfTickets(ticketDb.getNumberOfTickets());
            userTicketInfo.setSeatNumber(ticketDb.getSeatNumber());
            userTicketInfo.setTicketPrice(ticketDb.getTicketPrice());
            userTicketInfo.setActiveStatus(ticketDb.getActiveStatus());

            userTicketsInfo.add(userTicketInfo);
        }
        return new PageImpl<>(userTicketsInfo, pageable, userTicketsInfo.size());
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

        bookingValidator.validateFlightExistence(flightId);
        bookingValidator.validateUserExistence(userRepository
                .findIdByFirstNameAndLastName(bookingTicketDTO.getFirstName(), bookingTicketDTO.getLastName()));
        bookingValidator.validateSeatAvailability(flightRepository.findNumberOfFreeSeatsById(flightId), bookingTicketDTO.getCountOfTickets());

        if (bookingTicketDTO.getReturnTicket()) {
            flightReturningId = flightRepository.
                    findIdByParameters(airportsRepository.
                                    findIdByPortNameAndPortCity(bookingTicketDTO.getReturnPortNameFrom(), bookingTicketDTO.getReturnPortCityFrom()),
                            airportsRepository.findIdByPortNameAndPortCity(bookingTicketDTO.getReturnPortNameTo(), bookingTicketDTO.getReturnPortCityTo()),
                            bookingTicketDTO.getReturnDepartureTime());
            bookingValidator.validateFlightExistence(flightReturningId);
            bookingValidator.validateSeatAvailability(flightRepository.findNumberOfFreeSeatsById(flightReturningId), bookingTicketDTO.getReturnCountOfTickets());
        }

        Ticket ticket = new Ticket();
        Ticket returningTicket = bookingTicketDTO.getReturnTicket() ? new Ticket() : null;

        ticketFactory(ticket, returningTicket, flightId, flightReturningId, bookingTicketDTO);

        if (bookingValidator.validateBookingTime(bookingStartTime, BOOKING_TIME_LIMIT_MS)) {
            throw new BookingTimeExpiredException();
        }

        ticketRepository.save(ticket);

        if (bookingTicketDTO.getReturnTicket() && returningTicket != null) {
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
        ticket.setActiveStatus(false);

        flightRepository
                .updateNumberOfFreeSeatsById(ticket.getSeatNumber() - bookingTicketDTO.getCountOfTickets(), flightId);

        if (bookingTicketDTO.getReturnTicket()) {

            bookingValidator.validateFlightExistence(flightReturningId);
            bookingValidator.validateSeatAvailability(flightRepository.findNumberOfFreeSeatsById(flightReturningId), bookingTicketDTO.getReturnCountOfTickets());

            returningTicket.setFlightId(flightReturningId);
            returningTicket.setPassengerId(ticket.getPassengerId());
            returningTicket.setTicketPrice(flightRepository.findFlightPriceById(flightReturningId) * bookingTicketDTO.getReturnCountOfTickets());
            returningTicket.setSeatNumber(flightRepository.findNumberOfFreeSeatsById(flightReturningId));
            returningTicket.setNumberOfTickets(bookingTicketDTO.getReturnCountOfTickets());
            returningTicket.setActiveStatus(false);

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
            ticket.setActiveStatus(true);
            ticketRepository.saveAndFlush(ticket);
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
        bookingValidator.validateTicketExistence(ticketRepository.existsById(id));

        Optional<Ticket> ticket = ticketRepository.findById(id);

        if (paymentHistoryRepository.existsByTicketId(id)) {
            Optional<PaymentDTO> paymentDTO = paymentHistoryRepository.findByTicketId(id);
            refundPayment(ticket, paymentDTO);
        }

        flightRefund(ticket);
        ticketRepository.delete(ticket.get());
    }

    private void refundPayment(Optional<Ticket> ticket, Optional<PaymentDTO> paymentDTO) {
        cardInfo.setNumberCard(paymentDTO.get().getCardNumber());
        cardInfo.setBalance(cardInfo.getBalance() + ticket.get().getTicketPrice());
    }

    private void flightRefund(Optional<Ticket> ticket) {
        Optional<Flight> flight = flightRepository.findById(ticket.get().getFlightId());
        flight.get().setNumberOfFreeSeats(flight.get().getNumberOfFreeSeats() + ticket.get().getNumberOfTickets());
        flightRepository.saveAndFlush(flight.get());
    }

    @Scheduled(fixedRate = 60000)
    protected void deleteExpiredTickets() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Ticket> expiredTickets = ticketRepository.findByBookingExpirationTimeBefore(currentTime);
        ticketRepository.deleteAll(expiredTickets);
    }
}
