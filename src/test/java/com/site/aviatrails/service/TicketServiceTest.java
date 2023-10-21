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
import com.site.aviatrails.exception.InsufficientFunds;
import com.site.aviatrails.repository.AirportsRepository;
import com.site.aviatrails.repository.FlightRepository;
import com.site.aviatrails.repository.PaymentHistoryRepository;
import com.site.aviatrails.repository.TicketRepository;
import com.site.aviatrails.repository.UserRepository;
import com.site.aviatrails.validator.BookingValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    private final Long ID_VALUE = 5L;

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private PaymentHistoryRepository paymentHistoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardInfo cardInfo;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private AirportsRepository airportsRepository;

    @Mock
    private BookingValidator bookingValidator;

    static Ticket ticket = new Ticket();
    static Flight flight = new Flight();
    static Airline airline = new Airline();
    static Airport airportFrom = new Airport();
    static Airport airportTo = new Airport();

    @BeforeAll
    public static void beforeAll() {
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);

    }

    @Test
    public void getAllTicketsPageTest() {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        Page<Ticket> page = new PageImpl<>(tickets);

        when(ticketRepository.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

        Page<Ticket> actualPage = ticketRepository.findAll(pageable);

        assertEquals(page, actualPage);
    }

    @Test
    public void getUserTicketsInfoByIdTest() {

        Pageable pageable = Mockito.mock(Pageable.class);

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName("Test");

        UserTicketInfo userTicketInfo = new UserTicketInfo();

        List<Object[]> ticketWithDetails = new ArrayList<>();
        Object[] ticketDetails = new Object[5];
        ticketDetails[0] = ticket;
        ticketDetails[1] = flight;
        ticketDetails[2] = airline;
        ticketDetails[3] = airportFrom;
        ticketDetails[4] = airportTo;
        ticketWithDetails.add(ticketDetails);
        List<UserTicketInfo> userTicketsInfoList = new ArrayList<>();

        userTicketInfo.setFirstName("Test");
        userTicketsInfoList.add(userTicketInfo);

        when(ticketRepository.getTicketWithDetails(ID_VALUE)).thenReturn(ticketWithDetails);
        when(userRepository.findById(ID_VALUE)).thenReturn(Optional.of(userInfo));
        doNothing().when(bookingValidator).validateTicketExistenceByPassengerId(any());
        doNothing().when(bookingValidator).validateUserExistence(any());

        Page<UserTicketInfo> expectedPage = ticketService.getUserTicketsInfoById(ID_VALUE, pageable);
        Page<UserTicketInfo> actualPage = new PageImpl<>(userTicketsInfoList, pageable, userTicketsInfoList.size());

        assertEquals(expectedPage, actualPage);
    }

    @Test
    public void bookingTicketTest() {
        BookingTicketDTO bookingTicketDTO = new BookingTicketDTO();
        bookingTicketDTO.setFirstName("Test");
        bookingTicketDTO.setCountOfTickets(50);
        bookingTicketDTO.setReturnTicket(false);

        Long flightId = 123L;
        Long flightReturningId = 456L;

        when(flightRepository.findIdByParameters(any(), any(), any())).thenReturn(flightId);
        when(flightRepository.findIdByParameters(any(), any(), any())).thenReturn(flightReturningId);
        when(userRepository.findIdByFirstNameAndLastName(any(), any())).thenReturn(789L);
        when(flightRepository.findNumberOfFreeSeatsById(flightReturningId)).thenReturn(5);

        doNothing().when(bookingValidator).validateFlightExistence(flightReturningId);
        doNothing().when(bookingValidator).validateUserExistence(789L);
        doNothing().when(bookingValidator).validateSeatAvailability(5, 50);
        when(bookingValidator.validateBookingTime(anyLong(), eq(900000L))).thenReturn(false);

        when(flightRepository.updateNumberOfFreeSeatsById(any(), any())).thenReturn(null);
        when(flightRepository.updateNumberOfFreeSeatsById(any(), any())).thenReturn(null);

        when(ticketRepository.save(any())).thenReturn(ticket);

        ticketService.bookingTicket(bookingTicketDTO);

        verify(ticketRepository, times(1)).save(any());
        verify(flightRepository, times(2)).findNumberOfFreeSeatsById(anyLong());
        verify(flightRepository, times(1)).findFlightPriceById(anyLong());
    }

    @Test
    public void paymentTest() {
        Long passengerId = 1L;
        Ticket firstTicket = new Ticket();
        firstTicket.setId(1L);
        firstTicket.setTicketPrice(50);
        Ticket secondTicket = new Ticket();
        secondTicket.setId(2L);
        secondTicket.setTicketPrice(75);

        CardInfo cardInfo = new CardInfo();
        cardInfo.setBalance(100);

        List<Ticket> unpaidTickets = new ArrayList<>();
        unpaidTickets.add(firstTicket);
        unpaidTickets.add(secondTicket);

        when(ticketRepository.findByPassengerId(passengerId)).thenReturn(unpaidTickets);
        when(paymentHistoryRepository.existsByTicketId(anyLong())).thenReturn(false);

        assertThrows(InsufficientFunds.class, () -> ticketService.payment(passengerId, cardInfo));

        verify(ticketRepository, never()).saveAndFlush(any(Ticket.class));
        verify(paymentHistoryRepository, never()).save(any(PaymentDTO.class));
    }

    @Test
    public void refundTicketWithPaymentTest() {
        cardInfo = new CardInfo();

        Ticket newTicket = new Ticket();
        newTicket.setId(1L);
        newTicket.setTicketPrice(100);
        newTicket.setPassengerId(1L);
        newTicket.setFlightId(1L);
        newTicket.setNumberOfTickets(1);

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setTicketId(newTicket.getId());
        paymentDTO.setPassengerId(1L);
        paymentDTO.setCardNumber("123456789456");

        flight.setId(1L);
        flight.setNumberOfFreeSeats(10);

        Optional<Ticket> ticket = Optional.of(newTicket);
        Optional<PaymentDTO> payment = Optional.of(paymentDTO);

        when(ticketRepository.existsById(newTicket.getId())).thenReturn(true);
        when(ticketRepository.findById(newTicket.getId())).thenReturn(ticket);
        when(paymentHistoryRepository.existsByTicketId(newTicket.getId())).thenReturn(true);
        when(paymentHistoryRepository.findByTicketId(newTicket.getId())).thenReturn(payment);
        when(flightRepository.findById(anyLong())).thenReturn(Optional.of(flight));

        ticketService.refundTicket(newTicket.getId());

        verify(paymentHistoryRepository).findByTicketId(newTicket.getId());
        verify(bookingValidator).validateTicketExistence(true);
        verify(flightRepository).saveAndFlush(any(Flight.class));
        verify(ticketRepository).delete(ticket.get());
    }
}
