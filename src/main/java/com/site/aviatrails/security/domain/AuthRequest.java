package com.site.aviatrails.security.domain;

import lombok.Data;

@Data
public class AuthRequest {
    private String login;
    private String password;
}
