package com.hamadoutech.demominio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestion de Fichiers avec MinIO")
                        .version("1.0.0")
                        .description("""
                                API REST développée en Spring Boot pour gérer des fichiers dans un bucket MinIO.
                                
                                **Fonctionnalités :**
                                - Upload de fichiers (JPEG, PNG, PDF, TXT)
                                - Téléchargement de fichiers
                                - Listage des fichiers
                                - Suppression de fichiers
                                
                                **Limitations :**
                                - Taille maximale : 10 Mo
                                - Types autorisés : image/jpeg, image/png, application/pdf, text/plain
                                """)
                        .contact(new Contact()
                                .name("Hamadou Tech")
                                .email("contact@hamadoutech.com")
                                .url("https://hamadoutech.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Serveur de développement local"),
                        new Server()
                                .url("https://api.hamadoutech.com")
                                .description("Serveur de production (si applicable)")
                ));
    }
}

