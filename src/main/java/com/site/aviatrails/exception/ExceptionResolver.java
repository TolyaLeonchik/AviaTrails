package com.site.aviatrails.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(value = InsufficientFunds.class)
    public ResponseEntity<HttpStatus> InsufficientFunds() {
        log.info("InsufficientFunds! Insufficient funds in your account!");
        return new ResponseEntity<>(HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(value = TicketAlreadyPaidException.class)
    public ResponseEntity<HttpStatus> TicketAlreadyPaidException() {
        log.info("TicketAlreadyPaidException! The ticket has already been paid!");
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = BookingTimeExpiredException.class)
    public ResponseEntity<HttpStatus> BookingTimeExpiredException() {
        log.info("BookingTimeExpiredException! The time for booking tickets has expired!");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = FlightNotFoundException.class)
    public ResponseEntity<HttpStatus> FlightNotFoundException() {
        log.info("FlightNotFoundException! Flight not found or does not exist!");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = NoAvailableSeatsException.class)
    public ResponseEntity<HttpStatus> NoAvailableSeatsException() {
        log.info("NoAvailableSeatsException! No available seats!");
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NoTicketsFoundException.class)
    public ResponseEntity<HttpStatus> NoTicketsFoundException() {
        log.info("NoTicketsFoundException! The ticket was not found or it does not exist!");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = PaymentNotFoundException.class)
    public ResponseEntity<HttpStatus> PaymentNotFoundException() {
        log.info("PaymentNotFoundException! You didn't pay for the ticket!");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<HttpStatus> UserNotFoundException() {
        log.info("UserNotFoundException! User not found!");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AirportNotFoundException.class)
    public ResponseEntity<HttpStatus> AirportNotFoundException() {
        log.info("AirportNotFoundException! Airport not found!");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AirlineNotFoundException.class)
    public ResponseEntity<HttpStatus> AirlineNotFoundException() {
        log.info("AirlineNotFoundException! Airline not found!");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
