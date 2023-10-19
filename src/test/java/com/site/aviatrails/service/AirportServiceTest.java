package com.site.aviatrails.service;

import com.site.aviatrails.domain.Airport;
import com.site.aviatrails.repository.AirportsRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class AirportServiceTest {

    private final Long ID_VALUE = 5L;

    @InjectMocks
    private AirportService airportService;

    @Mock
    private AirportsRepository airportsRepository;

    @BeforeAll
    public static void beforeAll() {
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    public void getAirportsTest() {
        airportService.getAirports();
        Mockito.verify(airportsRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void getAirportByIdTest() {
        airportService.getAirport(ID_VALUE);
        Mockito.verify(airportsRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    public void createAirport() {
        Airport airport = new Airport();
        airportService.createAirport(airport);
        Mockito.verify(airportsRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void updateAirport() {
        airportService.updateAirport(new Airport());
        Mockito.verify(airportsRepository, Mockito.times(1)).saveAndFlush(any());
    }

    @Test
    public void deleteAirport() {
        airportService.deleteAirportById(ID_VALUE);
        Mockito.verify(airportsRepository, Mockito.times(1)).deleteById(anyLong());
    }
}
