package com.site.aviatrails.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.site.aviatrails.domain.CardInfo;
import com.site.aviatrails.domain.tickets.Ticket;
import com.site.aviatrails.domain.tickets.UserTicketInfo;
import com.site.aviatrails.security.WebSecurity;
import com.site.aviatrails.security.filter.JwtAuthenticationFilter;
import com.site.aviatrails.service.TicketService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TicketControllerTest {

    @MockBean
    TicketService ticketService;

    @MockBean
    private WebSecurity webSecurity;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;

    static List<Ticket> tickets = new ArrayList<>();
    static Ticket ticket = new Ticket();
    static UserTicketInfo userTicketInfo = new UserTicketInfo();
    static Long valueId = 5L;

    @BeforeAll
    public static void beforeAll() {
        ticket.setId(valueId);
        tickets.add(ticket);

        userTicketInfo.setFirstName("Test");
    }

    @Test
    public void getTicketsPageTest() throws Exception {
        Page<Ticket> ticketsPage = new PageImpl<>(tickets);
        when(ticketService.getAllTickets(Mockito.any(Pageable.class))).thenReturn(ticketsPage);

        mockMvc.perform(get("/booking/allTickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    public void getTicketsReturnsNotFoundWhenNoTicketsTest() throws Exception {
        Page<Ticket> ticketsPage = new PageImpl<>(new ArrayList<>());
        when(ticketService.getAllTickets(Mockito.any(Pageable.class))).thenReturn(ticketsPage);

        mockMvc.perform(get("/booking/allTickets"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getTicketByIdTest() throws Exception {
        List<UserTicketInfo> userTickets = new ArrayList<>();
        userTickets.add(userTicketInfo);
        Page<UserTicketInfo> ticketsPage = new PageImpl<>(userTickets);
        when(ticketService.getUserTicketsInfoById(anyLong(), Mockito.any(Pageable.class))).thenReturn(ticketsPage);

        mockMvc.perform(get("/booking/user/{id}", valueId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].firstName", Matchers.is("Test")));
    }

    @Test
    public void getUsersTicketsPageTest() throws Exception {
        List<UserTicketInfo> userTickets = new ArrayList<>();
        userTickets.add(userTicketInfo);
        userTickets.add(userTicketInfo);
        Page<UserTicketInfo> ticketsPage = new PageImpl<>(userTickets);

        when(webSecurity.currentUserid(any())).thenReturn(valueId);
        when(ticketService.getUserTicketsInfoById(anyLong(), Mockito.any(Pageable.class))).thenReturn(ticketsPage);

        mockMvc.perform(get("/booking/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    public void getUsersTicketsReturnsNotFoundWhenNoTickets() throws Exception {
        Page<UserTicketInfo> emptyPage = new PageImpl<>(new ArrayList<>());

        when(webSecurity.currentUserid(any())).thenReturn(valueId);
        when(ticketService.getUserTicketsInfoById(anyLong(), Mockito.any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/booking/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createTicketTest() throws Exception {
        TicketService mockUS = Mockito.mock(TicketService.class);
        Mockito.doNothing().when(mockUS).bookingTicket(any());

        mockMvc.perform(post("/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isCreated());

    }

    @Test
    public void paymentTickets_ReturnsCreated() throws Exception {
        CardInfo cardInfo = new CardInfo();

        Mockito.when(webSecurity.currentUserid(Mockito.any())).thenReturn(valueId);
        Mockito.doNothing().when(ticketService).payment(valueId, cardInfo);

        mockMvc.perform(post("/booking/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardInfo)))
                .andExpect(status().isCreated());
    }

    @Test
    public void refundTicketTest() throws Exception {
        TicketService mockUS = Mockito.mock(TicketService.class);
        Mockito.doNothing().when(mockUS).refundTicket(anyLong());

        mockMvc.perform(delete("/booking/refund/{id}", valueId))
                .andExpect(status().isNoContent());
    }
}
