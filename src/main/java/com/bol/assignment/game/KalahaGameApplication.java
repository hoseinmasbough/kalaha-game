package com.bol.assignment.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.bol.assignment.game.common")
public class KalahaGameApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(KalahaGameApplication.class, args);
    }
}
