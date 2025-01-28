package org.project.authservice.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openapi() {
        return new OpenAPI()
                .servers(
                        List.of(
                                new Server().url("http://localhost:8083")
                        )
                )
                .info(
                        new Info().title("Book Documentation").version("1.0")
                );
    }
}
