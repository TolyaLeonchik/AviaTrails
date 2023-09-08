package com.site.aviatrails.controller;

import com.site.aviatrails.domain.BookingTicketDTO;
import com.site.aviatrails.domain.Ticket;
import com.site.aviatrails.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class ReservationController {

    private final TicketService ticketService;

    public ReservationController(TicketService ticketService) {
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

    @PostMapping
    public ResponseEntity<HttpStatus> bookingTicket(@RequestBody BookingTicketDTO bookingTicketDTO) {
        ticketService.bookingTicket(bookingTicketDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
