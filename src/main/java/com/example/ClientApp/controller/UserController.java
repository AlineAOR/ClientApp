package com.example.ClientApp.controller;

import com.example.ClientApp.dto.AuthenticationRequest;
import com.example.ClientApp.dto.LoginResponse;
import com.example.ClientApp.dto.RegisterRequest;
import com.example.ClientApp.entities.UserEntity;
import com.example.ClientApp.repository.UserRepository;
import com.example.ClientApp.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        try {
            var userNamePassword = new UsernamePasswordAuthenticationToken(authenticationRequest.login(), authenticationRequest.password());
            var auth = this.authenticationManager.authenticate(userNamePassword);

            var token = tokenService.generateToken((UserEntity) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponse(token));
        } catch (AuthenticationException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequest registerRequest) {
        try {
            if (this.userRepository.findByLogin(registerRequest.login()) != null) {
                return ResponseEntity.badRequest().build();
            }

            String encryptedPassword = new BCryptPasswordEncoder().encode(registerRequest.password());
            UserEntity newUser = new UserEntity(registerRequest.login(), encryptedPassword, registerRequest.role());

            this.userRepository.save(newUser);

            return ResponseEntity.ok().build();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


