package com.site.aviatrails.exception;

public class TicketAlreadyPaidException extends RuntimeException {
    public TicketAlreadyPaidException() {
        super("The ticket has already been paid!");
    }
}
