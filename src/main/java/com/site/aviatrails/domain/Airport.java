package com.site.aviatrails.domain;

import jakarta.persistence.*;
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
    private String portName;

    @Column(name = "city")
    private String portCity;

    @Column(name = "country")
    private String portCountry;

    @Column(name = "airport_code")
    private String airportCode;
}
