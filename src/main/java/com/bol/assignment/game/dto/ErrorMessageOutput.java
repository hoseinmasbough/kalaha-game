package com.bol.assignment.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessageOutput
{
    private String message;
    private List<String> details;
}
