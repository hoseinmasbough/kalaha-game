package com.bol.assignment.game.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MessageConstant {
    INTERNAL_SERVER_ERROR("Internal server error"),
    RESOURCE_NOT_FOUND("Resource not found"),
    ENTITY_NOT_FOUND("Game id %s not found"),
    PIT_INCORRECT_INDEX("There isn't a pit with index %s"),
    BUSINESS_VALIDATION_ERROR("Business validation error"),
    INVALID_PAYLOAD("Invalid payload"),
    ARGUMENT_TYPE_MISMATCH("Argument type mismatch"),
    INVALID_ARGUMENT("Invalid argument"),
    INVALID_OBJECT("Invalid object"),
    INVALID_REQUEST("Invalid request"),
    REWARD_ANOTHER_TURN("You get another turn."),
    SELECT_BIG_PIT("The big pits couldn't be select"),
    SELECT_OPPOSITE_PIT("Player %s couldn't select from the opposite pits"),
    UTILITY_CLASS("Utility class couldn't is instantiated"),
    REWARD_CAPTURE_OPPOSITE_STONES("You've captured all stones in the opposite pit");

    @Getter
    private final String message;

}
