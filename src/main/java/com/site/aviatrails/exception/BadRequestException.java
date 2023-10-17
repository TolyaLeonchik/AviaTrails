package com.site.aviatrails.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super("BadRequest!");
    }
}
