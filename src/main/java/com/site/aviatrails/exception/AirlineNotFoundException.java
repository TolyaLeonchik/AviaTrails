package com.site.aviatrails.exception;

public class AirlineNotFoundException extends RuntimeException {
    public AirlineNotFoundException() {
        super("Airline not found!");
    }
}
