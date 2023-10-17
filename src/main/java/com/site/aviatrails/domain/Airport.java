package com.site.aviatrails.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Entity(name = "airports")
public class Airport {

    @Id
    @SequenceGenerator(name = "SeqGenPort", sequenceName = "airports_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "SeqGenPort")
    private Long id;

    @Column(name = "port_name")
    @Pattern(regexp = "^[A-ZА-я].*")
    private String portName;

    @Column(name = "city")
    @Pattern(regexp = "^[A-ZА-я].*")
    private String portCity;

    @Column(name = "country")
    @Pattern(regexp = "^[A-ZА-я].*")
    private String portCountry;

    @Column(name = "airport_code")
    @Pattern(regexp = "^[A-Z]+$")
    private String airportCode;
}
