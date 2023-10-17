package com.site.aviatrails.exception;

public class AirportNotFoundException extends RuntimeException {
    public AirportNotFoundException() {
        super("Airport not found!");
    }
}
