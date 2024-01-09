package com.example.ClientApp.serviceTest;

import com.example.ClientApp.entities.UserEntity;
import com.example.ClientApp.entities.UserRoles;
import com.example.ClientApp.repository.UserRepository;
import com.example.ClientApp.service.AuthorizationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class AuthorizationServiceTest {
    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private UserRepository repository;

    public AuthorizationServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        String username = "testUser";
        UserEntity userEntity = new UserEntity(username, "encodedPassword", UserRoles.USER);
        when(repository.findByLogin(username)).thenReturn(userEntity);

        UserDetails userDetails = authorizationService.loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void testLoadUserByUsernameUserNotFound() {
        String username = "nonexistentUser";
    }
}