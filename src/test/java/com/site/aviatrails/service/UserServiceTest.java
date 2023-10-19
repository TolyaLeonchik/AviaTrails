package com.site.aviatrails.service;

import com.site.aviatrails.domain.UserInfo;
import com.site.aviatrails.repository.UserRepository;
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
public class UserServiceTest {

    private final Long ID_VALUE = 5L;
    private final String PHONE_NUMBER = "574159658742365";

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeAll
    public static void beforeAll() {
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    public void getUsersTest() {
        userService.getUsers();
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void getUserTest() {
        userService.getUser(ID_VALUE);
        Mockito.verify(userRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    public void createUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setPhoneNumber(PHONE_NUMBER);
        userService.createUser(userInfo);
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void updateUser() {
        userService.updateUser(new UserInfo());
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(any());
    }

    @Test
    public void deleteUser() {
        userService.deleteUserById(ID_VALUE);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(anyLong());
    }
}
