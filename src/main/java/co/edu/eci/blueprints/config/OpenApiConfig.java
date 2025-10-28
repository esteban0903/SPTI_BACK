package co.edu.eci.blueprints.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI api() {
        final String BEARER = "bearer-jwt";
        return new OpenAPI()
          .info(new Info().title("BluePrints API")
            .version("2.0")
            .description("Parte 2 — Seguridad con JWT (OAuth 2.0)"))
          .addSecurityItem(new SecurityRequirement().addList(BEARER))
          .components(new Components().addSecuritySchemes(BEARER,
            new SecurityScheme()
              .name(BEARER)
              .type(SecurityScheme.Type.HTTP)
              .scheme("bearer")
              .bearerFormat("JWT")));
    }
}
