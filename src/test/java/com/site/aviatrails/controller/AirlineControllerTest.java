package com.site.aviatrails.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.site.aviatrails.domain.Airline;
import com.site.aviatrails.security.filter.JwtAuthenticationFilter;
import com.site.aviatrails.service.AirlineService;
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
@WebMvcTest(value = AirlineController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AirlineControllerTest {

    @MockBean
    AirlineService airlineService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;

    static List<Airline> airlines = new ArrayList<>();
    static Airline airline = new Airline();
    static Long valueId = 5L;

    @BeforeAll
    public static void beforeAll() {
        airline.setId(valueId);
        airlines.add(airline);
    }

    @Test
    public void getAirlinesTest() throws Exception {
        Mockito.when(airlineService.getAirlines()).thenReturn(airlines);

        mockMvc.perform(get("/airline"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(valueId.intValue())));
    }

    @Test
    public void getAirlinesByIdTest() throws Exception {
        Mockito.when(airlineService.getAirline(valueId)).thenReturn(Optional.of(airline));

        mockMvc.perform(get("/airline/{id}", valueId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(valueId.intValue())));
    }

    @Test
    public void createAirlineTest() throws Exception {
        AirlineService mockUS = Mockito.mock(AirlineService.class);
        Mockito.doNothing().when(mockUS).createAirline(any());

        mockMvc.perform(post("/airline")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(airline)))
                .andExpect(status().isCreated());

    }

    @Test
    public void updateAirlineTest() throws Exception {
        AirlineService mockUS = Mockito.mock(AirlineService.class);
        Mockito.doNothing().when(mockUS).updateAirline(any());

        mockMvc.perform(put("/airline")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(airline)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteAirlineTest() throws Exception {
        AirlineService mockUS = Mockito.mock(AirlineService.class);
        Mockito.doNothing().when(mockUS).deleteAirlineById(anyLong());

        mockMvc.perform(delete("/airline/{id}", valueId))
                .andExpect(status().isNoContent());
    }
}
