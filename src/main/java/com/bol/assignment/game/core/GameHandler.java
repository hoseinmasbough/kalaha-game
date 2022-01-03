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

    /**
     * Create a new game.
     *
     * @param stonesCount Count of stones in any small pits. It should be more than one
     * @return Return a {@link GameStatus} that always reflect the latest status of the game
     */
    public static GameStatus createGame(int stonesCount) {
        GameStatus gameStatus = new GameStatus();
        gameStatus.setActivePlayer(PlayerType.PLAYER_1);
        gameStatus.setPits(initializePits(gameStatus, stonesCount));
        log.debug("initializing a new game with {} stones and {} pits", stonesCount, GameConstant.ALL_PITS.getValue());
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

    /**
     * It picks up all the stones in specified pit and sows the stones on to the right.
     *
     * @return If the specified pit has no stone it doesn't do any sowing and return false, otherwise true
     */
    public boolean sow() {
        Pit currentSmallPit = getPit(currentPitIndex);
        if (currentSmallPit.isEmpty()) {
            log.info("pit {} is empty so it couldn't have any sow", currentPitIndex);
            return false;
        }

        int stones = currentSmallPit.takeStones();
        IntStream.rangeClosed(1, stones)
                .forEach(index -> addStoneToNextPit());
        return true;
    }

    /**
     * It changes the player turn, If the last pit is not a big pit
     */
    public void checkPlayerTurn() {
        if (PitUtil.isBigPit(currentPitIndex)) {
            gameStatus.setReward(MessageConstant.REWARD_ANOTHER_TURN.getMessage());
        } else {
            changeNextPlayer();
        }
    }

    private void addStoneToNextPit() {
        goToNextPit();
        if (PitUtil.isPlayerOnOppositeBigPit(currentPitIndex, gameStatus.getActivePlayer())) {
            log.debug("Current pit is an opposite big pit. Skip it. pitIndex:{}", currentPitIndex);
            goToNextPit();
        }

        Pit targetPit = getPit(currentPitIndex);
        targetPit.sow();
        log.info("pit {} has {} stones.", currentPitIndex, targetPit.getStones());
    }

    /**
     * Checking if there are conditions of capturing all stones of the opposite pit then to takes them and adding to big pit.
     */
    public void checkLastSownPitForCapturing() {
        // todo: There are a lot of if-condition statements in this method. It could be simpler and refactored by some best practices like RuleEngine
        // https://www.baeldung.com/java-replace-if-statements
        if (PitUtil.isBigPit(currentPitIndex)) {
            return;
        }
        boolean isLastPitForActivePlayer = PitUtil.isPitForActivePlayer(currentPitIndex, gameStatus.getActivePlayer());
        if (!isLastPitForActivePlayer) {
            return;
        }
        log.info("Owner of last pit {} is activePlayer.", currentPitIndex);
        Pit lastPit = getPit(currentPitIndex);
        // It means it had no stone before sowing
        if (lastPit.getStones() > 1) {
            return;
        }
        Pit oppositePit = getOppositePit(currentPitIndex);
        if (oppositePit.isEmpty()) {
            return;
        }
        log.info("last pit {} is empty and opposite pit {} isn't empty, so capturing all opposite's stones.", lastPit.getIndex(), oppositePit.getIndex());
        int oppositeStones = oppositePit.takeStones();
        int lastPitStones = lastPit.takeStones();
        oppositeStones += lastPitStones;
        addStonesToActivePlayerBigPit(oppositeStones);
    }

    private void goToNextPit() {
        currentPitIndex++;
        if (currentPitIndex == GameConstant.ALL_PITS.getValue()) {
            currentPitIndex = 0;
        }
        log.debug("go to the next pit. pitIndex:{}", currentPitIndex);
    }

    private Pit getOppositePit(int pitIndex) {
        int oppositeIndex = PitUtil.getOppositePitIndex(pitIndex);
        return getPit(oppositeIndex);
    }

    /**
     * Review the finishing conditions of game
     *
     * @return If game has status of finishing it returns true, otherwise false
     */
    public boolean isGameFinished() {
        List<Pit> playerOnePits = getAllSmallPitsByPlayerType(PlayerType.PLAYER_1);
        List<Pit> playerTwoPits = getAllSmallPitsByPlayerType(PlayerType.PLAYER_2);

        playerOneRemainingStones = getRemainingStones(playerOnePits);
        playerTwoRemainingStones = getRemainingStones(playerTwoPits);
        log.debug("Does game have finishing condition? p1RemainingStones:{}, p2RemainingStones:{}",
                playerOneRemainingStones, playerTwoRemainingStones);

        return playerOneRemainingStones == 0 || playerTwoRemainingStones == 0;
    }

    /**
     * It reviews of stones count of both players and determine the winner or maybe game is draw
     */
    public void finishGame() {
        log.debug("Game finishing is started");
        if (playerOneRemainingStones > 0) {
            putRemainingStonesToBigPit(PlayerType.PLAYER_1);
        } else if (playerTwoRemainingStones > 0) {
            putRemainingStonesToBigPit(PlayerType.PLAYER_2);
        }

        PlayerType winner = determineWinner();
        log.info("Game is finished and winner is {}", winner);
        gameStatus.setWinner(winner);
    }

    private PlayerType determineWinner() {
        int playerOneBigPitIndex = PitUtil.getBigPitIndexByPlayer(PlayerType.PLAYER_1);
        Pit playerOneBigPit = getPit(playerOneBigPitIndex);

        int playerTwoBigPitIndex = PitUtil.getBigPitIndexByPlayer(PlayerType.PLAYER_2);
        Pit playerTwoBigPit = getPit(playerTwoBigPitIndex);
        log.info("player one has {} stones and player two has {}", playerOneBigPit.getStones(),
                playerTwoBigPit.getStones());
        if (playerOneBigPit.getStones() == playerTwoBigPit.getStones()) {
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
        log.info("big pit {} has {} stones", bigPitIndex, bigPit.getStones());
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
        log.debug("next player is {}", this.gameStatus.getActivePlayer());
    }

    private List<Pit> getAllSmallPitsByPlayerType(PlayerType playerType) {
        return playerType == PlayerType.PLAYER_1 ? this.gameStatus.getPits().subList(GameConstant.PLAYER_ONE_FIRST_PIT_INDEX.getValue(), GameConstant.LEFT_BIG_PIT_INDEX.getValue()) :
                this.gameStatus.getPits().subList(GameConstant.PLAYER_TWO_FIRST_PIT_INDEX.getValue(), GameConstant.RIGHT_BIG_PIT_INDEX.getValue());
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
