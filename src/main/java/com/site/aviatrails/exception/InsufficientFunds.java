package com.site.aviatrails.exception;

public class InsufficientFunds extends RuntimeException {
    public InsufficientFunds() {
        super("Insufficient funds in your account!");
    }
}
