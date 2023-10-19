package com.site.aviatrails.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.site.aviatrails.domain.Airport;
import com.site.aviatrails.security.filter.JwtAuthenticationFilter;
import com.site.aviatrails.service.AirportService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = AirportController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AirportControllerTest {

    @MockBean
    AirportService airportService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;

    static List<Airport> airports = new ArrayList<>();
    static Airport airport = new Airport();
    static Long valueId = 5L;

    @BeforeAll
    public static void beforeAll() {
        airport.setId(valueId);
        airports.add(airport);
    }

    @Test
    public void getAirportsTest() throws Exception {
        Mockito.when(airportService.getAirports()).thenReturn(airports);

        mockMvc.perform(get("/airport"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(valueId.intValue())));
    }

    @Test
    public void getAirportByIdTest() throws Exception {
        Mockito.when(airportService.getAirport(valueId)).thenReturn(Optional.of(airport));

        mockMvc.perform(get("/airport/{id}", valueId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(valueId.intValue())));
    }

    @Test
    public void createAirportTest() throws Exception {
        AirportService mockUS = Mockito.mock(AirportService.class);
        Mockito.doNothing().when(mockUS).createAirport(any());

        mockMvc.perform(post("/airport")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(airport)))
                .andExpect(status().isCreated());

    }

    @Test
    public void updateAirportTest() throws Exception {
        AirportService mockUS = Mockito.mock(AirportService.class);
        Mockito.doNothing().when(mockUS).updateAirport(any());

        mockMvc.perform(put("/airport")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(airport)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteAirportTest() throws Exception {
        AirportService mockUS = Mockito.mock(AirportService.class);
        Mockito.doNothing().when(mockUS).deleteAirportById(anyLong());

        mockMvc.perform(delete("/airport/{id}", valueId))
                .andExpect(status().isNoContent());
    }
}
