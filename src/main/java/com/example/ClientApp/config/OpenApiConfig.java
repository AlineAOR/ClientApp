package com.example.ClientApp.config;

import io.swagger.v3.oas.models.OpenAPI;

import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Client-Application")
                        .version("1.0.0")
                        .description("API REST, para cadastro de pessoas com score e endereço.\n" +
                                "Uma pessoa possui: Nome, idade, cep, estado, cidade, bairro, logradouro, telefone, score de 0 a 1000.\n"));
    }
}
