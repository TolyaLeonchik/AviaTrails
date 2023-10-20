package com.site.aviatrails.service;

import com.site.aviatrails.domain.Airline;
import com.site.aviatrails.domain.Airport;
import com.site.aviatrails.domain.Flight;
import com.site.aviatrails.domain.FlightInfo;
import com.site.aviatrails.repository.AirlinesRepository;
import com.site.aviatrails.repository.AirportsRepository;
import com.site.aviatrails.repository.FlightRepository;
import com.site.aviatrails.validator.FlightValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    private static final Long ID_VALUE = 5L;

    @InjectMocks
    private FlightService flightService;

    @Mock
    private AirportsRepository airportsRepository;

    @Mock
    private AirlinesRepository airlinesRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightValidator flightValidator;

    static Flight flight = new Flight();
    static Airline airline = new Airline();
    static Airport from = new Airport();
    static Airport to = new Airport();
    static FlightInfo expected = new FlightInfo();
    static List<FlightInfo> flightsInfoList = new ArrayList<>();
    static String cityNameFrom = "TestCityA";
    static String cityNameTo = "TestCityB";
    static String direction = "asc";
    static String property = "flightPrice";

    @BeforeAll
    public static void beforeAll() {
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);

        flight.setAirlineId(ID_VALUE);
        flight.setFromAirportId(ID_VALUE);
        flight.setToAirportId(6L);
        flight.setDepartureTime(LocalDateTime.of(2023, 10, 20, 15, 30));
        flight.setArrivalTime(LocalDateTime.of(2023, 10, 20, 17, 30));

        airline.setId(ID_VALUE);

        from.setPortCity(cityNameFrom);
        to.setPortCity(cityNameTo);

        expected.setPortCityFrom("TestCityA");
        expected.setPortCityTo("TestCityB");
        expected.setDepartureTime(LocalDateTime.of(2023, 10, 20, 15, 30));
        expected.setArrivalTime(LocalDateTime.of(2023, 10, 20, 17, 30));
    }

    @Test
    public void getFlightTest() {
        flightService.getAllFlights();
        Mockito.verify(flightRepository, times(1)).findAll();
    }

    @Test
    public void getFlightByIdTest() {
        flightService.findById(ID_VALUE);
        Mockito.verify(flightRepository, times(1)).findById(anyLong());
    }

    @Test
    public void getFlightInfoByIdTest() {
        FlightInfo expectedFlightInfo = new FlightInfo();

        when(flightRepository.findById(flight.getId())).thenReturn(Optional.of(flight));
        when(airportsRepository.findById(Mockito.any())).thenReturn(Optional.of(new Airport()));
        when(airlinesRepository.findById(Mockito.any())).thenReturn(Optional.of(new Airline()));

        expectedFlightInfo.setAirline(airlinesRepository.findById(flight.getAirlineId()).get().getAirlineName());
        expectedFlightInfo.setDepartureTime(flightRepository.findById(flight.getId()).get().getDepartureTime());
        expectedFlightInfo.setArrivalTime(flightRepository.findById(flight.getId()).get().getArrivalTime());


        FlightInfo resultFlightInfo = flightService.findInfoById(flight.getId());

        assertEquals(expectedFlightInfo.getAirline(), resultFlightInfo.getAirline());
        assertEquals(expectedFlightInfo.getDepartureTime(), resultFlightInfo.getDepartureTime());
        assertEquals(expectedFlightInfo.getArrivalTime(), resultFlightInfo.getArrivalTime());
    }

    @Test
    public void findByParameters() {

        List<Long> cityOfDeparture = new ArrayList<>();
        cityOfDeparture.add(2L);
        List<Long> cityOfArrival = new ArrayList<>();
        cityOfArrival.add(3L);
        List<Flight> flightSearchByParameters = new ArrayList<>();
        flightSearchByParameters.add(flight);

        FlightInfo flightInfoNew = new FlightInfo();
        flightInfoNew.setPortCityFrom("TestCityA");
        flightInfoNew.setPortCityTo("TestCityB");
        flightInfoNew.setDepartureTime(LocalDateTime.of(2023, 10, 20, 15, 30));
        flightInfoNew.setArrivalTime(LocalDateTime.of(2023, 10, 20, 17, 30));
        flightInfoNew.setFlightDurationInHours(2D);
        flightsInfoList.add(flightInfoNew);
        Page<FlightInfo> expectedPage = new PageImpl<>(flightsInfoList, PageRequest.of(0, 10, Sort.Direction.ASC, property), flightsInfoList.size());

        when(airportsRepository.findIdsByPortCity(eq("TestCityA"))).thenReturn(cityOfDeparture);
        when(airportsRepository.findIdsByPortCity(eq("TestCityB"))).thenReturn(cityOfArrival);
        when(airportsRepository.findById(flight.getFromAirportId())).thenReturn(Optional.of(from));
        when(airportsRepository.findById(flight.getToAirportId())).thenReturn(Optional.of(to));
        when(airlinesRepository.findById(anyLong())).thenReturn(Optional.of(airline));
        when(flightRepository.findByCityFromAndCityToAndLocalDate(eq(cityOfDeparture), eq(cityOfArrival), any(LocalDate.class)))
                .thenReturn(flightSearchByParameters);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, property);

        Page<FlightInfo> actualPage = flightService.findByParameters(pageable, cityNameFrom, cityNameTo, LocalDate.of(2023, 10, 20), direction, property);

        assertEquals(expectedPage, actualPage);
    }

    @Test
    public void createFlightTest() {
        Mockito.doNothing().when(flightValidator).validateFlight(flight);

        flightService.createFlight(flight);
        Mockito.verify(flightRepository, times(1)).save(any());
    }

    @Test
    public void updateFlightTest() {
        Mockito.doNothing().when(flightValidator).validateFlight(flight);

        flightService.updateFlight(flight);
        Mockito.verify(flightRepository, times(1)).saveAndFlush(any());
    }

    @Test
    public void deleteFlightTest() {
        flightService.deleteFlightById(ID_VALUE);
        Mockito.verify(flightRepository, times(1)).deleteById(anyLong());
    }
}
