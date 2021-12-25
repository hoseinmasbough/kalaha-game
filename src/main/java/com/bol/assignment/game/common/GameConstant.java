package com.bol.assignment.game.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GameConstant {
    ALL_PITS(14),
    LEFT_BIG_PIT_INDEX(6),
    RIGHT_BIG_PIT_INDEX(13),
    PLAYER_TWO_LAST_PIT_INDEX(12),
    PLAYER_ONE_FIRST_PIT_INDEX(0),
    PLAYER_TWO_FIRST_PIT_INDEX(7);

    private final int value;
}
