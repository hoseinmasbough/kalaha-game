package com.bol.assignment.game.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@ConfigurationProperties(prefix = "game.kalaha.pit")
@Getter
@Setter
public class GameSettings {

    @NotNull(message = "Stones count should be set")
    private Integer stones;
}
