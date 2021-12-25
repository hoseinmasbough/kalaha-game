package com.bol.assignment.game.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class GameSpecifications {

    @Value("${game.kalaha.pit.stones}")
    private Integer stones;
}
