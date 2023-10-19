package com.site.aviatrails.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.site.aviatrails.domain.UserInfo;
import com.site.aviatrails.security.filter.JwtAuthenticationFilter;
import com.site.aviatrails.service.UserService;
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
@WebMvcTest(value = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @MockBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;

    static List<UserInfo> users = new ArrayList<>();
    static UserInfo userInfo = new UserInfo();
    static Long value = 5L;

    @BeforeAll
    public static void beforeAll() {
        userInfo.setId(value);
        users.add(userInfo);
    }

    @Test
    public void getUsers() throws Exception {
        Mockito.when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(value.intValue())));
    }

    @Test
    public void getUserTest() throws Exception {
        Mockito.when(userService.getUser(value)).thenReturn(Optional.of(userInfo));

        mockMvc.perform(get("/user/{id}", value))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(value.intValue())));
    }

    @Test
    public void createUserTest() throws Exception {
        UserService mockUS = Mockito.mock(UserService.class);
        Mockito.doNothing().when(mockUS).createUser(any());

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfo)))
                .andExpect(status().isCreated());

    }

    @Test
    public void updateUserTest() throws Exception {
        UserService mockUS = Mockito.mock(UserService.class);
        Mockito.doNothing().when(mockUS).updateUser(any());

        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfo)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteUserTest() throws Exception {
        UserService mockUS = Mockito.mock(UserService.class);
        Mockito.doNothing().when(mockUS).deleteUserById(anyLong());

        mockMvc.perform(delete("/user/{id}", value))
                .andExpect(status().isNoContent());
    }
}
