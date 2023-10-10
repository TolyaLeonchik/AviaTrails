package com.site.aviatrails.validator;

public class PhoneNumberValidator {

    private static final String PHONE_NUMBER_REGEX = "^(\\+)?\\d{12,15}$";

    public static void validate(String phoneNumber) {

        if (!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
            throw new IllegalArgumentException("Invalid phone number format!");
        }
    }
}
