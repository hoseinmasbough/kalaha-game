package com.bol.assignment.game.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "game.kalaha.pit")
@Getter
@Setter
public class GameSettings {

    @NotBlank
    private Integer stones;
}
