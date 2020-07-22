package com.example.springjwtlogin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Value("${application.client.url}")
    private String clientUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.info("Registering cors mapping for {}", clientUrl);
        registry.addMapping("/**").allowedOrigins(clientUrl);
    }
}
