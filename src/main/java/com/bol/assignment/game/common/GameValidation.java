package com.bol.assignment.game.common;

import com.bol.assignment.game.core.PlayerType;
import com.bol.assignment.game.exception.BusinessValidationException;

public final class GameValidation {

    private GameValidation(){
        throw new IllegalStateException(MessageConstant.UTILITY_CLASS.getMessage());
    }

    public static void validateOnSelectedPit(PlayerType activePlayer, int pitIndex) {
        if (pitIndex < GameConstant.PLAYER_ONE_FIRST_PIT_INDEX.getValue() || pitIndex > GameConstant.RIGHT_BIG_PIT_INDEX.getValue()) {
            throw new BusinessValidationException(String.format(MessageConstant.PIT_INCORRECT_INDEX.getMessage(), pitIndex));
        }

        if (pitIndex == GameConstant.LEFT_BIG_PIT_INDEX.getValue() || pitIndex == GameConstant.RIGHT_BIG_PIT_INDEX.getValue()) {
            throw new BusinessValidationException(MessageConstant.SELECT_BIG_PIT.getMessage());
        }

        if (activePlayer == PlayerType.PLAYER_1 && pitIndex > GameConstant.LEFT_BIG_PIT_INDEX.getValue()) {
            throw new BusinessValidationException(String.format(MessageConstant.SELECT_OPPOSITE_PIT.getMessage(), 1));
        }

        if (activePlayer == PlayerType.PLAYER_2 && pitIndex < GameConstant.LEFT_BIG_PIT_INDEX.getValue()) {
            throw new BusinessValidationException(String.format(MessageConstant.SELECT_OPPOSITE_PIT.getMessage(), 2));
        }
    }
}
