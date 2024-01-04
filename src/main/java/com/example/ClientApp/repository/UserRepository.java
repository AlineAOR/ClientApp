package com.example.ClientApp.repository;

import com.example.ClientApp.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository <UserEntity,Integer> {

   UserDetails findByLogin(String login);
}
