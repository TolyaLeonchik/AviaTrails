package com.site.aviatrails.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException() {
        super("You didn't pay for the ticket!");
    }
}
