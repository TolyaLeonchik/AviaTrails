package com.site.aviatrails;

import com.site.aviatrails.controller.AirlineController;
import com.site.aviatrails.controller.AirportController;
import com.site.aviatrails.controller.FlightController;
import com.site.aviatrails.controller.TicketController;
import com.site.aviatrails.controller.UserController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AviaTrailsApplicationTests {

    @Autowired
    public UserController userController;

    @Autowired
    public TicketController ticketController;

    @Autowired
    public FlightController flightController;

    @Autowired
    public AirlineController airlineController;

    @Autowired
    public AirportController airportController;

    @Test
    void contextLoads() {
        Assertions.assertThat(userController).isNotNull();
        Assertions.assertThat(ticketController).isNotNull();
        Assertions.assertThat(flightController).isNotNull();
        Assertions.assertThat(airlineController).isNotNull();
        Assertions.assertThat(airportController).isNotNull();
    }

}
