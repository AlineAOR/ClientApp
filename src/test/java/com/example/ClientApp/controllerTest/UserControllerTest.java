package com.example.ClientApp.controllerTest;

import com.example.ClientApp.controller.UserController;
import com.example.ClientApp.dto.AuthenticationRequest;
import com.example.ClientApp.dto.LoginResponse;
import com.example.ClientApp.dto.RegisterRequest;
import com.example.ClientApp.entities.UserEntity;
import com.example.ClientApp.entities.UserRoles;
import com.example.ClientApp.repository.UserRepository;
import com.example.ClientApp.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserControllerTest() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(userController, "authenticationManager", authenticationManager);
        ReflectionTestUtils.setField(userController, "userRepository", userRepository);
        ReflectionTestUtils.setField(userController, "tokenService", tokenService);
    }

    @Test
    void testLoginSuccess() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("username", "password");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new UserEntity("username", "encodedPassword", UserRoles.USER));
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(tokenService.generateToken(any())).thenReturn("generatedToken");

        ResponseEntity responseEntity = userController.login(authenticationRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("generatedToken", ((LoginResponse) responseEntity.getBody()).token());
    }



    @Test
    void testRegisterSuccess() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("newUsername", "newPassword", UserRoles.USER);
        when(userRepository.findByLogin("newUsername")).thenReturn(null);

        ResponseEntity responseEntity = userController.register(registerRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testRegisterUserExists() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("existingUsername", "password", UserRoles.USER);
        when(userRepository.findByLogin("existingUsername")).thenReturn(new UserEntity());

        ResponseEntity responseEntity = userController.register(registerRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(userRepository, never()).save(any(UserEntity.class));
    }


}
