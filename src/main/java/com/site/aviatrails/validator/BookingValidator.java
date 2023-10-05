package com.site.aviatrails.validator;

import com.site.aviatrails.domain.PaymentDTO;
import com.site.aviatrails.domain.tickets.Ticket;
import com.site.aviatrails.exception.*;

import java.util.List;
import java.util.Optional;

public class BookingValidator {


    public void validateFlightExistence(Long flightId) {
        if (flightId == null) {
            throw new FlightNotFoundException();
        }
    }

    public void validateSeatAvailability(int totalSeats, int bookedSeats) {
        int availableSeats = totalSeats - bookedSeats;

        if (availableSeats < 0) {
            throw new NoAvailableSeatsException();
        }
    }

    public boolean validateBookingTime(long bookingStartTime, long bookingTimeLimit) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - bookingStartTime;
        return elapsedTime > bookingTimeLimit;
    }

    public void validateUserExistence(Long passengerId) {
        if (passengerId == null) {
            throw new UserNotFoundException();
        }
    }

    public void validateTicketExistence(boolean ticketExists) {
        if (!ticketExists) {
            throw new NoTicketsFoundException();
        }
    }

    public void validatePaymentExistence(boolean paymentExists) {
        if (!paymentExists) {
            throw new PaymentNotFoundException();
        }
    }

    public void validateTicketExistenceByPassengerId(List<Long> tickets) {
        if (tickets.isEmpty()) {
            throw new NoTicketsFoundException();
        }
    }
}
