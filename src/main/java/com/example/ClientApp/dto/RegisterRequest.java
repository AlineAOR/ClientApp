package com.example.ClientApp.dto;

import com.example.ClientApp.entities.UserRoles;

public record RegisterRequest(String login, String password, UserRoles role) {
}
