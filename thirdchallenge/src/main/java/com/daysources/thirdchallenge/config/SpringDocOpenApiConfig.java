package com.daysources.thirdchallenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI openAPI () {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("API - User Management and Notify Microservices")
                                .description("A set of microservice containers for managing users, addresses and passwords." +
                                        "Possesses integrations with ViaCEP, MongoDB, MySQL and RabbitMQ, and runs via Docker.")
                                .version("v1.2")
                );
    }
}
