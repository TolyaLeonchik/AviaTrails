package com.site.aviatrails.exception;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException() {
        super("Flight not found or does not exist!");
    }
}
