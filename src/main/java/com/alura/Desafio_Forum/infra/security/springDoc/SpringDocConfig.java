package com.alura.Desafio_Forum.infra.security.springDoc;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("Desafio Alura - Fórum API")
                        .description("API Rest de uma aplicação de Fórum, contendo as funcionalidades de CRUD de usuários, de cursos, e tópicos onde usuários podem responder cada tópico")
                        .contact(new Contact()
                                .name("Michel Braga")
                                .email("michel.diener@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://github.com/mbraga2023/Desafio-Alura-Forum-Api/blob/master/LICENSE")));
    }
}
