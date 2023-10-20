package com.site.aviatrails.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.site.aviatrails.domain.Flight;
import com.site.aviatrails.domain.FlightInfo;
import com.site.aviatrails.security.filter.JwtAuthenticationFilter;
import com.site.aviatrails.service.FlightService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = FlightController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FlightControllerTest {

    @MockBean
    FlightService flightService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;

    static List<Flight> flights = new ArrayList<>();
    static List<FlightInfo> flightsInfoList = new ArrayList<>();
    static Flight flight = new Flight();
    static FlightInfo flightInfo = new FlightInfo();
    static Long valueId = 5L;
    static String testName = "TestName";

    @BeforeAll
    public static void beforeAll() {
        flight.setId(valueId);
        flights.add(flight);

        flightInfo.setAirline(testName);
        flightsInfoList.add(flightInfo);
    }

    @Test
    public void getFlightsTest() throws Exception {
        Mockito.when(flightService.getAllFlights()).thenReturn(flights);

        mockMvc.perform(get("/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(valueId.intValue())));
    }

    @Test
    public void getAllFlightsPageTest() throws Exception {
        Page<FlightInfo> page = new PageImpl<>(flightsInfoList);
        Mockito.when(flightService.getFlightsPagesBySort(any(Pageable.class), anyString(), anyString())).thenReturn(page);

        mockMvc.perform(get("/flights/allPage")
                        .param("direction", "asc")
                        .param("property", "propertyName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].airline")
                        .value(testName));
    }

    @Test
    public void getFlightByParametersTest() throws Exception {
        Page<FlightInfo> page = new PageImpl<>(flightsInfoList);
        Mockito.when(flightService.findByParameters(any(Pageable.class), anyString(), anyString(), any(LocalDate.class),
                anyString(), anyString())).thenReturn(page);

        mockMvc.perform(get("/flights/search")
                        .param("cityOfDeparture", "TestCity")
                        .param("cityOfArrival", "TestCity")
                        .param("date", "2023-08-06")
                        .param("direction", "asc")
                        .param("property", "propertyName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].airline")
                        .value(testName));
    }

    @Test
    public void getFlightByIdTest() throws Exception {
        Mockito.when(flightService.findById(valueId)).thenReturn(Optional.of(flight));

        mockMvc.perform(get("/flights/{id}", valueId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(valueId.intValue())));
    }

    @Test
    public void getFlightInfoByIdTest() throws Exception {
        Mockito.when(flightService.findInfoById(valueId)).thenReturn(flightInfo);

        mockMvc.perform(get("/flights/info/{id}", valueId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.airline", Matchers.is(testName)));
    }

    @Test
    public void createFlightTest() throws Exception {
        FlightService mockUS = Mockito.mock(FlightService.class);
        Mockito.doNothing().when(mockUS).createFlight(any());

        mockMvc.perform(post("/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isCreated());

    }

    @Test
    public void updateFlightTest() throws Exception {
        FlightService mockUS = Mockito.mock(FlightService.class);
        Mockito.doNothing().when(mockUS).updateFlight(any());

        mockMvc.perform(put("/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteFlightTest() throws Exception {
        FlightService mockUS = Mockito.mock(FlightService.class);
        Mockito.doNothing().when(mockUS).deleteFlightById(anyLong());

        mockMvc.perform(delete("/flights/{id}", valueId))
                .andExpect(status().isNoContent());
    }
}
