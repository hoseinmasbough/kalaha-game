package com.bol.assignment.game;

import com.bol.assignment.game.common.GameConstant;
import com.bol.assignment.game.core.PitUtil;
import com.bol.assignment.game.core.PlayerType;
import com.bol.assignment.game.entity.GameStatus;
import com.bol.assignment.game.entity.Pit;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestUtil {

    public static GameStatus createGameStatusInInit(int stonesCount){
        GameStatus gameStatus = createSimpleGameStatus(PlayerType.PLAYER_1);
        List<Pit> pits = IntStream.range(0, GameConstant.ALL_PITS.getValue())
                .mapToObj(index -> {
                    if (PitUtil.isBigPit(index)) {
                        return new Pit(index, 0, null);
                    } else {
                        return new Pit(index, stonesCount, null);
                    }
                })
                .collect(Collectors.toList());

        gameStatus.setPits(pits);
        return gameStatus;
    }

    public static GameStatus createGameStatusForCaptureAllReward(){
        GameStatus gameStatus = createSimpleGameStatus(PlayerType.PLAYER_1);
        List<Pit> pits = Arrays.asList(
                new Pit(0, 2, null),
                new Pit(1, 1, null),
                new Pit(2, 0, null),
                new Pit(3, 2, null),
                new Pit(4, 11, null),
                new Pit(5, 1, null),
                new Pit(6, 1, null),
                new Pit(7, 2, null),
                new Pit(8, 2, null),
                new Pit(9, 2, null),
                new Pit(10, 2, null),
                new Pit(11, 2, null),
                new Pit(12, 2, null),
                new Pit(13, 6, null));
        gameStatus.setPits(pits);
        return gameStatus;
    }

    public static GameStatus createGameStatusForWiningPlayerOne(){
        GameStatus gameStatus = createSimpleGameStatus(PlayerType.PLAYER_1);
        List<Pit> pits = Arrays.asList(
                new Pit(0, 0, null),
                new Pit(1, 0, null),
                new Pit(2, 0, null),
                new Pit(3, 0, null),
                new Pit(4, 0, null),
                new Pit(5, 2, null),
                new Pit(6, 40, null),
                new Pit(7, 1, null),
                new Pit(8, 1, null),
                new Pit(9, 1, null),
                new Pit(10, 1, null),
                new Pit(11, 1, null),
                new Pit(12, 0, null),
                new Pit(13, 25, null));
        gameStatus.setPits(pits);
        return gameStatus;
    }

    public static GameStatus createGameStatusForWiningPlayerTwo(){
        GameStatus gameStatus = createSimpleGameStatus(PlayerType.PLAYER_2);
        List<Pit> pits = Arrays.asList(
                new Pit(0, 1, null),
                new Pit(1, 1, null),
                new Pit(2, 1, null),
                new Pit(3, 1, null),
                new Pit(4, 1, null),
                new Pit(5, 0, null),
                new Pit(6, 25, null),
                new Pit(7, 0, null),
                new Pit(8, 0, null),
                new Pit(9, 0, null),
                new Pit(10, 0, null),
                new Pit(11, 0, null),
                new Pit(12, 2, null),
                new Pit(13, 40, null));
        gameStatus.setPits(pits);
        return gameStatus;
    }

    public static GameStatus createGameStatusForDraw(){
        GameStatus gameStatus = createSimpleGameStatus(PlayerType.PLAYER_2);
        List<Pit> pits = Arrays.asList(
                new Pit(0, 1, null),
                new Pit(1, 1, null),
                new Pit(2, 1, null),
                new Pit(3, 1, null),
                new Pit(4, 1, null),
                new Pit(5, 0, null),
                new Pit(6, 30, null),
                new Pit(7, 0, null),
                new Pit(8, 0, null),
                new Pit(9, 0, null),
                new Pit(10, 0, null),
                new Pit(11, 0, null),
                new Pit(12, 2, null),
                new Pit(13, 35, null));
        gameStatus.setPits(pits);
        return gameStatus;
    }

    private static GameStatus createSimpleGameStatus(PlayerType playerType){
        GameStatus gameStatus = new GameStatus();
        gameStatus.setId(0);
        gameStatus.setActivePlayer(playerType);
        return gameStatus;
    }
}
