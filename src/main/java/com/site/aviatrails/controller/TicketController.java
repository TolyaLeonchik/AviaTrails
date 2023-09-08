package com.site.aviatrails.controller;

import com.site.aviatrails.domain.tickets.BookingTicketDTO;
import com.site.aviatrails.domain.tickets.Ticket;
import com.site.aviatrails.domain.tickets.UserTicketInfo;
import com.site.aviatrails.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<UserTicketInfo>> getUsersTickets(@PathVariable Long id) {
        List<UserTicketInfo> tickets = ticketService.getUserTicketsInfo(id);
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<HttpStatus> bookingTicket(@RequestBody BookingTicketDTO bookingTicketDTO) {
        ticketService.bookingTicket(bookingTicketDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
