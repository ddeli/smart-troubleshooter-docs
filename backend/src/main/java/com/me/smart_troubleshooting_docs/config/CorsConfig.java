package com.me.smart_troubleshooting_docs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Erlaube Anfragen von deinem Frontend
        config.addAllowedOrigin("http://localhost:4200");

        // Erlaube alle HTTP-Methoden (GET, POST, PUT, DELETE, etc.)
        config.addAllowedMethod("*");

        // Erlaube alle Header
        config.addAllowedHeader("*");

        // Erlaube Cookies und Authentifizierungsdaten
        config.setAllowCredentials(true);

        // Setze die Konfiguration für alle Pfade
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}