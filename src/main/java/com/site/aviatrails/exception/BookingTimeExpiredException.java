package com.site.aviatrails.exception;

public class BookingTimeExpiredException extends RuntimeException {
    public BookingTimeExpiredException() {
        super("The time for booking tickets has expired!");
    }
}
