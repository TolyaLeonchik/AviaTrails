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
@Entity(name = "airlines")
public class Airline {

    @Id
    @SequenceGenerator(name = "SeqGenLines", sequenceName = "airlines_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "SeqGenLines")
    private Long id;

    @Column(name = "airline_name")
    @Pattern(regexp = "^[A-ZА-я].*")
    private String airlineName;

    @Column(name = "country")
    @Pattern(regexp = "^[A-ZА-я].*")
    private String airlineCountry;

    @Column(name = "port_id")
    private Long airportId;
}
