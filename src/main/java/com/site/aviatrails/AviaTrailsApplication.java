package com.site.aviatrails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
