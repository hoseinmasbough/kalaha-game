package com.bol.assignment.game.core;

import com.bol.assignment.game.common.GameConstant;
import com.bol.assignment.game.common.MessageConstant;
import com.bol.assignment.game.entity.GameStatus;
import com.bol.assignment.game.entity.Pit;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class GameHandler {
    private final GameStatus gameStatus;
    private int playerOneRemainingStones = 0;
    private int playerTwoRemainingStones = 0;
    private int currentPitIndex;

    public GameHandler(GameStatus gameStatus, int currentPitIndex) {
        this.gameStatus = gameStatus;
        this.currentPitIndex = currentPitIndex;
    }

    public static GameStatus createGame(int stonesCount) {
        GameStatus gameStatus = new GameStatus();
        gameStatus.setActivePlayer(PlayerType.PLAYER_1);
        gameStatus.setPits(initializePits(gameStatus, stonesCount));
        return gameStatus;
    }

    private static List<Pit> initializePits(GameStatus gameStatus, int stonesCount) {
        return IntStream.range(0, GameConstant.ALL_PITS.getValue())
                .mapToObj(index -> {
                    if (PitUtil.isBigPit(index)) {
                        return new Pit(index, 0, gameStatus);
                    } else {
                        return new Pit(index, stonesCount, gameStatus);
                    }
                })
                .collect(Collectors.toList());
    }

    public boolean sow() {
        cleanReward();

        Pit currentSmallPit = getPit(currentPitIndex);
        if (currentSmallPit.isEmpty()) {
            return false;
        }

        int stones = currentSmallPit.takeStones();
        IntStream.rangeClosed(1, stones)
                .forEach(index -> addStoneToNextPit(index == stones));
        return true;
    }

    public void checkPlayerTurn(){
        if (PitUtil.isBigPit(currentPitIndex)) {
            gameStatus.setReward(MessageConstant.REWARD_ANOTHER_TURN.getMessage());
        } else {
            changeNextPlayer();
        }
    }

    private void addStoneToNextPit(boolean lastStone) {
        goToNextPit();
        if (PitUtil.isPlayerOnOppositeBigPit(currentPitIndex, gameStatus.getActivePlayer())) {
            log.debug("Current pit is an opposite big pit. Skip it. pitIndex:{}", currentPitIndex);
            goToNextPit();
        }

        Pit targetPit = getPit(currentPitIndex);

        if (!lastStone || PitUtil.isBigPit(currentPitIndex)) {
            targetPit.sow();
            log.info("pit {} has {} stones.", currentPitIndex, targetPit.getStones());
            return;
        }

        boolean isLastPitForActivePlayer = PitUtil.isPitForActivePlayer(currentPitIndex, gameStatus.getActivePlayer());
        if (isLastPitForActivePlayer) {
            log.info("last pit {} is my own.", currentPitIndex);
            Pit oppositeSmallPit = getOppositePit(currentPitIndex);
            if (targetPit.isEmpty() && !oppositeSmallPit.isEmpty()) {
                log.info("last pit {} is empty and opposite pit {} isn't empty, so capturing all opposite's stones.", targetPit.getIndex(), oppositeSmallPit.getIndex());
                int oppositeStones = oppositeSmallPit.takeStones();
                oppositeStones += 1;
                addStonesToActivePlayerBigPit(oppositeStones);
                return;
            }
        }
        targetPit.sow();
        log.info("pit {} has {} stones.", currentPitIndex, targetPit.getStones());
    }

    private void goToNextPit() {
        currentPitIndex++;
        if (currentPitIndex == GameConstant.ALL_PITS.getValue()) {
            currentPitIndex = 0;
        }
        log.debug("go to next pit. pitIndex:{}", currentPitIndex);
    }

    private Pit getOppositePit(int pitIndex) {
        int oppositeIndex = PitUtil.getOppositePitIndex(pitIndex);
        return getPit(oppositeIndex);
    }

    public boolean isGameFinished() {
        List<Pit> playerOnePits = getAllSmallPitsByPlayerType(PlayerType.PLAYER_1);
        List<Pit> playerTwoPits = getAllSmallPitsByPlayerType(PlayerType.PLAYER_2);

        playerOneRemainingStones = getRemainingStones(playerOnePits);
        playerTwoRemainingStones = getRemainingStones(playerTwoPits);
        log.debug("does game have finishing condition? p1RemainingStones:{}, p2RemainingStones:{}",
                playerOneRemainingStones, playerTwoRemainingStones);

        return playerOneRemainingStones == 0 || playerTwoRemainingStones == 0;
    }

    public void finishGame() {
        log.info("The game should be finished");
        if (playerOneRemainingStones > 0) {
            putRemainingStonesToBigPit(PlayerType.PLAYER_1);
        } else if (playerTwoRemainingStones > 0) {
            putRemainingStonesToBigPit(PlayerType.PLAYER_2);
        }

        PlayerType winner = determineWinner();
        log.info("And FINALLY, Our WINNER is {}", winner);
        gameStatus.setWinner(winner);
    }

    private PlayerType determineWinner() {
        int playerOneBigPitIndex = PitUtil.getBigPitIndexByPlayer(PlayerType.PLAYER_1);
        Pit playerOneBigPit = getPit(playerOneBigPitIndex);

        int playerTwoBigPitIndex = PitUtil.getBigPitIndexByPlayer(PlayerType.PLAYER_2);
        Pit playerTwoBigPit = getPit(playerTwoBigPitIndex);

        if(playerOneBigPit.getStones() == playerTwoBigPit.getStones()){
            return PlayerType.NOBODY;
        }
        return playerOneBigPit.getStones() > playerTwoBigPit.getStones() ? PlayerType.PLAYER_1 : PlayerType.PLAYER_2;
    }

    private void putRemainingStonesToBigPit(PlayerType playerType) {
        List<Pit> playerPits = getAllSmallPitsByPlayerType(playerType);
        int collectedStones = takeRemainingStones(playerPits);
        log.info("take {} stones for {} to put in the big pit", collectedStones, playerType);
        int bigPitIndex = PitUtil.getBigPitIndexByPlayer(playerType);
        Pit bigPit = getPit(bigPitIndex);
        bigPit.addStones(collectedStones);
        log.info("big pit of {} has {} stones", playerType, bigPit.getStones());
    }

    private int getRemainingStones(List<Pit> pits) {
        return pits.stream().mapToInt(Pit::getStones).sum();
    }

    private int takeRemainingStones(List<Pit> pits) {
        return pits.stream()
                .filter(pit -> !pit.isEmpty())
                .mapToInt(Pit::takeStones)
                .sum();
    }

    private void addStonesToActivePlayerBigPit(int stones) {
        int bigPitIndex = PitUtil.getBigPitIndexByPlayer(gameStatus.getActivePlayer());
        log.info("add {} stones to big pit {}", stones, bigPitIndex);
        Pit bigPit = getPit(bigPitIndex);
        bigPit.addStones(stones);
        log.info("big pit {} has {} stones", bigPitIndex, stones);
        gameStatus.setReward(MessageConstant.REWARD_CAPTURE_OPPOSITE_STONES.getMessage());
    }

    private Pit getPit(Integer index) {
        return this.gameStatus.getPits().get(index);
    }

    private void changeNextPlayer() {
        if (this.gameStatus.getActivePlayer() == PlayerType.PLAYER_1) {
            this.gameStatus.setActivePlayer(PlayerType.PLAYER_2);
        } else {
            this.gameStatus.setActivePlayer(PlayerType.PLAYER_1);
        }
    }

    private List<Pit> getAllSmallPitsByPlayerType(PlayerType playerType) {
        return playerType == PlayerType.PLAYER_1 ? this.gameStatus.getPits().subList(GameConstant.PLAYER_ONE_FIRST_PIT_INDEX.getValue(), GameConstant.LEFT_BIG_PIT_INDEX.getValue()) :
                this.gameStatus.getPits().subList(GameConstant.PLAYER_TWO_FIRST_PIT_INDEX.getValue(), GameConstant.RIGHT_BIG_PIT_INDEX.getValue());
    }

    private void cleanReward() {
        gameStatus.setReward(null);
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
