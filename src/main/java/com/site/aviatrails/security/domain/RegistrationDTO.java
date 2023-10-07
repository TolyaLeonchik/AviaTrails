package com.site.aviatrails.security.domain;

import lombok.Data;

@Data
public class RegistrationDTO {
    public String firstName;
    public String lastName;
    private String email;
    private String phoneNumber;
    public String userLogin;
    public String userPassword;
}
