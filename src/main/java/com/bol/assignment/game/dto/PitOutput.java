package com.bol.assignment.game.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "List of all pits status")
public class PitOutput {
    @ApiModelProperty(notes = "Pit index")
    private int index;
    @ApiModelProperty(notes = "Count of stones")
    private int stones;
}
