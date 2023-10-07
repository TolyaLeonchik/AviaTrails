package com.site.aviatrails.security.domain;

import com.site.aviatrails.domain.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Entity(name = "security_credentials")
public class SecurityCredentials {

    @Id
    @SequenceGenerator(name = "mySeqGenSec", sequenceName = "security_credentials_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "mySeqGenSec")
    private Long id;

    @Column(name = "user_login")
    private String userLogin;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role userRole;

    @Column(name = "user_id")
    private Long userId;
}
