package com.site.aviatrails.controller;

import com.site.aviatrails.domain.CardInfo;
import com.site.aviatrails.domain.tickets.BookingTicketDTO;
import com.site.aviatrails.domain.tickets.Ticket;
import com.site.aviatrails.domain.tickets.UserTicketInfo;
import com.site.aviatrails.exception.InsufficientFunds;
import com.site.aviatrails.security.WebSecurity;
import com.site.aviatrails.service.TicketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking")
@SecurityRequirement(name = "Bearer Authentication")
public class TicketController {

    private final TicketService ticketService;
    private final WebSecurity webSecurity;

    public TicketController(TicketService ticketService, WebSecurity webSecurity) {
        this.ticketService = ticketService;
        this.webSecurity = webSecurity;
    }

    @GetMapping("/allTickets")
    public ResponseEntity<Page<Ticket>> getTickets(@PageableDefault Pageable pageable) {
        Page<Ticket> tickets = ticketService.getAllTickets(pageable);
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Page<UserTicketInfo>> getUsersTicketsById(@PathVariable Long id, @PageableDefault Pageable pageable) {
        Page<UserTicketInfo> tickets = ticketService.getUserTicketsInfoById(id, pageable);
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Page<UserTicketInfo>> getUsersTickets(@PageableDefault Pageable pageable) {
        Long currentId = webSecurity.currentUserid(SecurityContextHolder.getContext().getAuthentication());
        Page<UserTicketInfo> tickets = ticketService.getUserTicketsInfoById(currentId, pageable);
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

    @PostMapping("/pay")
    public ResponseEntity<HttpStatus> paymentTickets(@RequestBody CardInfo cardInfo) throws InsufficientFunds {
        Long currentUserid = webSecurity.currentUserid(SecurityContextHolder.getContext().getAuthentication());
        ticketService.payment(currentUserid, cardInfo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/refund/{id}")
    public ResponseEntity<HttpStatus> refundTicket(@PathVariable Long id) {
        ticketService.refundTicket(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
