package com.site.aviatrails.exception;

public class NoAvailableSeatsException extends RuntimeException {
    public NoAvailableSeatsException() {
        super("No available seats!");
    }
}
