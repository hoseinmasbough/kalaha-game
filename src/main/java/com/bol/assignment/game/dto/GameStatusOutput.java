package com.bol.assignment.game.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(description = "Latest full status of the kalaha game")
public class GameStatusOutput {
    @ApiModelProperty(notes = "Game id")
    private String id;
    @ApiModelProperty(notes = "Which player should be play")
    private String activePlayer;
    @ApiModelProperty(notes = "Reward of player")
    private String reward;
    @ApiModelProperty(notes = "Winner of the game")
    private String winner;
    @ApiModelProperty(notes = "Status of pits")
    private List<PitOutput> pits;
}
