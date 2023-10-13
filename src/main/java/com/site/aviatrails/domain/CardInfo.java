package com.site.aviatrails.domain;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class CardInfo {

    private String firstName;
    private String lastName;
    @Digits(integer = 16, fraction = 0)
    @Size(min = 16, max = 16)
    private String numberCard;
    private Integer balance;
}
