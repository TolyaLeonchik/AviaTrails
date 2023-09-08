package com.site.aviatrails.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
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
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    //TODO: подумать про взрослые/дети билеты
}
