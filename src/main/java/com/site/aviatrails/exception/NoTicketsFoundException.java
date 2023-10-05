package com.site.aviatrails.exception;

public class NoTicketsFoundException extends RuntimeException {
    public NoTicketsFoundException() {
        super("The ticket was not found or it does not exist!");
    }
}
