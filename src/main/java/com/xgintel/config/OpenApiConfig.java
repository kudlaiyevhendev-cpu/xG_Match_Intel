package com.xgintel.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI metadata. The generated spec (at {@code /v3/api-docs}, Swagger UI at
 * {@code /swagger-ui.html}) is the single source of truth the agent's tool
 * definitions are derived from.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI xgMatchIntelOpenApi() {
        return new OpenAPI().info(new Info()
                .title("xG Match Intel API")
                .version("v1")
                .description("Football analytics API: teams, matches, xG metrics, and an LLM agent.")
                .license(new License().name("Data: StatsBomb Open Data (non-commercial, attribution required)")));
    }
}
