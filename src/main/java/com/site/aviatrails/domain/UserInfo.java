package com.site.aviatrails.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Entity(name = "user_info")
public class UserInfo {

    @Id
    @SequenceGenerator(name = "SeqGen", sequenceName = "user_info_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "SeqGen")
    private Long id;

    @Column(name = "first_name")
    @Pattern(regexp = "^[A-ZА-я].*")
    private String firstName;

    @Column(name = "last_name")
    @Pattern(regexp = "^[A-ZА-я].*")
    private String lastName;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "phone_number")
    @Size(min = 12, max = 15)
    private String phoneNumber;
}
