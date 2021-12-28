package com.bol.assignment.game.core;

import com.bol.assignment.game.common.GameConstant;
import com.bol.assignment.game.common.MessageConstant;

public final class PitUtil {

    private PitUtil() {
        throw new IllegalStateException(MessageConstant.UTILITY_CLASS.getMessage());
    }

    public static boolean isPlayerOnOppositeBigPit(int currentPitIndex, PlayerType activePlayer) {
        return (currentPitIndex == GameConstant.RIGHT_BIG_PIT_INDEX.getValue() && activePlayer == PlayerType.PLAYER_1) ||
                (currentPitIndex == GameConstant.LEFT_BIG_PIT_INDEX.getValue() && activePlayer == PlayerType.PLAYER_2);
    }

    public static boolean isPitForActivePlayer(int pitIndex, PlayerType activePlayer) {
        return (pitIndex < GameConstant.LEFT_BIG_PIT_INDEX.getValue() && activePlayer == PlayerType.PLAYER_1) ||
                (pitIndex > GameConstant.LEFT_BIG_PIT_INDEX.getValue() && activePlayer == PlayerType.PLAYER_2);
    }


    public static boolean isBigPit(int pitIndex) {
        return pitIndex == GameConstant.RIGHT_BIG_PIT_INDEX.getValue() || pitIndex == GameConstant.LEFT_BIG_PIT_INDEX.getValue();
    }

    public static int getBigPitIndexByPlayer(PlayerType activePlayer) {
        return activePlayer == PlayerType.PLAYER_1 ? GameConstant.LEFT_BIG_PIT_INDEX.getValue() : GameConstant.RIGHT_BIG_PIT_INDEX.getValue();
    }

    public static int getOppositePitIndex(int pitIndex) {
        return GameConstant.PLAYER_TWO_LAST_PIT_INDEX.getValue() - pitIndex;
    }
}
