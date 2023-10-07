package com.site.aviatrails;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
        info = @Info(
                title = "AviaTrail project",
                description = "This is my diploma project",
                contact = @Contact(
                        name = "Leonchik Tolya",
                        email = "leonchik_tolik@mail.ru",
                        url = "@leonchikanatoliy"
                )
        )
)
@SpringBootApplication
public class AviaTrailsApplication {

    static final Logger log = LoggerFactory.getLogger(AviaTrailsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AviaTrailsApplication.class, args);

        log.error("ERROR LOG!!!");
        log.info("INFO LOG");
        log.debug("DEBUG LOG");
    }

}
