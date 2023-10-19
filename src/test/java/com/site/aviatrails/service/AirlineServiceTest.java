package com.site.aviatrails.service;

import com.site.aviatrails.domain.Airline;
import com.site.aviatrails.repository.AirlinesRepository;
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
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class AirlineServiceTest {

    private final Long ID_VALUE = 5L;

    @InjectMocks
    private AirlineService airlineService;

    @Mock
    private AirlinesRepository airlinesRepository;

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
    public void getAirlinesTest() {
        airlineService.getAirlines();
        Mockito.verify(airlinesRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void getAirlineByIdTest() {
        airlineService.getAirline(ID_VALUE);
        Mockito.verify(airlinesRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    public void createAirline() {
        AirportsRepository airportsRepositoryMock = Mockito.mock(AirportsRepository.class);
        AirlinesRepository airlinesRepositoryMock = Mockito.mock(AirlinesRepository.class);

        AirlineService airlineService = new AirlineService(airlinesRepositoryMock, airportsRepositoryMock);

        Mockito.when(airportsRepositoryMock.existsById(eq(ID_VALUE))).thenReturn(true);

        Airline airline = new Airline();
        airline.setAirportId(ID_VALUE);
        airlineService.createAirline(airline);
        Mockito.verify(airportsRepositoryMock, Mockito.times(1)).existsById(eq(ID_VALUE));
        Mockito.verify(airlinesRepositoryMock, Mockito.times(1)).save(any());
    }

    @Test
    public void updateAirline() {
        Mockito.when(airportsRepository.existsById(eq(ID_VALUE))).thenReturn(true);
        Airline airline = new Airline();
        airline.setAirportId(ID_VALUE);
        airlineService.updateAirline(airline);
        Mockito.verify(airportsRepository, Mockito.times(1)).existsById(eq(ID_VALUE));
        Mockito.verify(airlinesRepository, Mockito.times(1)).saveAndFlush(any());
    }

    @Test
    public void deleteAirline() {
        airlineService.deleteAirlineById(ID_VALUE);
        Mockito.verify(airlinesRepository, Mockito.times(1)).deleteById(anyLong());
    }
}
