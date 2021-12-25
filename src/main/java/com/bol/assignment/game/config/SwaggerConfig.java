package com.bol.assignment.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bol.assignment.game.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Kalaha Game Api service",
                "This application provides api for building Kalaha game application.",
                "0.1",
                null,
                new Contact("Seyyed Hossein Masbough", "https://www.linkedin.com/in/hosein-masbough/", "hosein.engineer@gmail.com"),
                null,
                null,
                Collections.emptyList()
        );
    }
}
