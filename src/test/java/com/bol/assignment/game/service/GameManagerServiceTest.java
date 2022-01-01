package com.bol.assignment.game.service;

import com.bol.assignment.game.TestUtil;
import com.bol.assignment.game.common.GameConstant;
import com.bol.assignment.game.common.GameSettings;
import com.bol.assignment.game.common.MessageConstant;
import com.bol.assignment.game.core.PitUtil;
import com.bol.assignment.game.core.PlayerType;
import com.bol.assignment.game.dto.GameStatusOutput;
import com.bol.assignment.game.entity.GameStatus;
import com.bol.assignment.game.exception.BusinessValidationException;
import com.bol.assignment.game.exception.ResourceNotFoundException;
import com.bol.assignment.game.mapper.GameStatusMapper;
import com.bol.assignment.game.repository.GameRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameManagerServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameSettings gameSettings;

    @InjectMocks
    GameManagerService gameManagerService;

    private static final int STONES_COUNT = 6;
    private long gameId = 0;

    @Test
    @Order(1)
    void should_return_gameStatusOutput_when_createGame() {
        //Mock
        Mockito.when(gameRepository.save(ArgumentMatchers.any(GameStatus.class))).then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(gameSettings.getStones()).thenReturn(STONES_COUNT);

        //Operation
        GameStatusOutput gameStatusOutput = gameManagerService.createGame();

        //Assertion
        assertNotNull(gameStatusOutput, "gameStatusOutput should not be null");
        gameId = gameStatusOutput.getId();
        assertEquals(0, gameId, "gameStatusOutput has a valid gameId");
        assertEquals(gameStatusOutput.getActivePlayer(),
                PlayerType.PLAYER_1.name(),
                "At the first the firstPlayer is active player");
        assertNull(gameStatusOutput.getReward(), "the reward is null in the first of the game");
        assertNull(gameStatusOutput.getWinner(), "the winner is null in the first of the game");
        assertNotNull(gameStatusOutput.getPits(), "pits should not be null");
        assertEquals(gameStatusOutput.getPits().size(),
                GameConstant.ALL_PITS.getValue(),
                "the size of pits should be " + GameConstant.ALL_PITS.getValue());
        for (int i = 0; i < GameConstant.ALL_PITS.getValue(); i++) {
            int stonesCount = gameStatusOutput.getPits().get(i).getStones();
            if (i == GameConstant.LEFT_BIG_PIT_INDEX.getValue() ||
                    i == GameConstant.RIGHT_BIG_PIT_INDEX.getValue()) {
                assertEquals(0, stonesCount,
                        "big stone is empty in the beginning");
            } else {
                assertEquals(STONES_COUNT, stonesCount,
                        "small stone has stones in the beginning");
            }
        }
    }

    @Test
    @Order(2)
    void should_return_same_gameStatus_when_choose_emptyPit() {
        //Mock
        GameStatus gameStatus = TestUtil.createGameStatusForCaptureAllReward();
        Mockito.when(gameRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(gameStatus));
        GameStatusOutput oldGameStatus = GameStatusMapper.INSTANCE.entityToOutput(gameStatus);

        //Operation
        GameStatusOutput newGameStatus = gameManagerService.sow(gameId, 2);

        //Assertion
        assertNotNull(newGameStatus, "gameStatus is not null");
        assertEquals(oldGameStatus, newGameStatus, "new and old should be same");
    }

    @Test
    @Order(2)
    void should_return_businessValidationException_when_choose_notExistedPit() {
        //Mock
        GameStatus gameStatus = TestUtil.createGameStatusForCaptureAllReward();
        Mockito.when(gameRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(gameStatus));

        //Operation
        int wrongPitIndex = GameConstant.RIGHT_BIG_PIT_INDEX.getValue() + 10;
        BusinessValidationException thrown = Assertions.assertThrows(BusinessValidationException.class,
                () -> gameManagerService.sow(gameId, wrongPitIndex),
                "Because of selecting a wrong pit index a BusinessValidationException was expected");

        //Assertion
        Assertions.assertEquals(String.format(MessageConstant.PIT_INCORRECT_INDEX.getMessage(), wrongPitIndex),
                thrown.getMessage());
    }

    @Test
    @Order(2)
    void should_return_businessValidationException_when_choose_bigPit() {
        //Mock
        int bigPitIndex = GameConstant.RIGHT_BIG_PIT_INDEX.getValue();
        GameStatus gameStatus = TestUtil.createGameStatusForCaptureAllReward();
        Mockito.when(gameRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(gameStatus));

        //Operation
        BusinessValidationException thrown = Assertions.assertThrows(BusinessValidationException.class,
                () -> gameManagerService.sow(gameId, bigPitIndex),
                "Because of selecting a big pit index a BusinessValidationException was expected");

        //Assertion
        Assertions.assertEquals(MessageConstant.SELECT_BIG_PIT.getMessage(), thrown.getMessage());
    }

    @Test
    @Order(2)
    void should_return_businessValidationException_when_choose_oppositePit() {
        //Mock
        GameStatus gameStatus = TestUtil.createGameStatusForCaptureAllReward();
        Mockito.when(gameRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(gameStatus));

        //Operation
        int playerTwoPitIndex = GameConstant.PLAYER_TWO_FIRST_PIT_INDEX.getValue();
        BusinessValidationException thrown = Assertions.assertThrows(BusinessValidationException.class,
                () -> gameManagerService.sow(gameId, playerTwoPitIndex),
                "Because of selecting an opposite pit index a BusinessValidationException was expected");

        //Assertion
        Assertions.assertEquals(String.format(MessageConstant.SELECT_OPPOSITE_PIT.getMessage(), 1),
                thrown.getMessage());
    }

    @Test
    @Order(2)
    void should_return_gameStatusOutput_with_anotherTurnReward_when_choose_firstPit_at_beginOfGame() {
        //Mock
        Mockito.when(gameRepository.save(ArgumentMatchers.any(GameStatus.class))).
                then(AdditionalAnswers.returnsFirstArg());
        GameStatus gameStatus = TestUtil.createGameStatusInInit(STONES_COUNT);
        Mockito.when(gameRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(gameStatus));

        //Operation
        GameStatusOutput gameStatusOutput = gameManagerService.sow(gameId,
                GameConstant.PLAYER_ONE_FIRST_PIT_INDEX.getValue());

        //Assertion
        assertNotNull(gameStatusOutput, "gameStatusOutput should not be null");
        assertEquals(gameId, gameStatusOutput.getId(), "returned gameId is equal to passed gameId");
        assertEquals(gameStatusOutput.getActivePlayer(), PlayerType.PLAYER_1.name(),
                "the first player is active player again");
        assertEquals(MessageConstant.REWARD_ANOTHER_TURN.getMessage(),
                gameStatusOutput.getReward(), "the firstPlayer has a reward");
        assertNull(gameStatusOutput.getWinner(), "the winner is null");
        assertNotNull(gameStatusOutput.getPits(), "pits should not be null");
        assertEquals(gameStatusOutput.getPits().size(),
                GameConstant.ALL_PITS.getValue(),
                "the size of pits should be " + GameConstant.ALL_PITS.getValue());
        for (int i = 0; i < GameConstant.ALL_PITS.getValue(); i++) {
            int stonesCount = gameStatusOutput.getPits().get(i).getStones();
            if (i == GameConstant.PLAYER_ONE_FIRST_PIT_INDEX.getValue()) {
                assertEquals(0, stonesCount,
                        "the first stone is empty");
            } else if (i < GameConstant.LEFT_BIG_PIT_INDEX.getValue()) {
                assertEquals(STONES_COUNT + 1, stonesCount,
                        "stones has stones in the current situation");
            } else if (i == GameConstant.LEFT_BIG_PIT_INDEX.getValue()) {
                assertEquals(1, stonesCount,
                        "the left big stone has one stone");
            } else if (i < GameConstant.RIGHT_BIG_PIT_INDEX.getValue()) {
                assertEquals(STONES_COUNT, stonesCount,
                        "remaining small stone has stones in the beginning");
            } else {
                assertEquals(0, stonesCount,
                        "remaining small stone has stones in the beginning");
            }
        }
    }

    @Test
    @Order(2)
    void should_return_gameStatusOutput_with_captureAllReward_when_lastPitIsEmpty() {
        //Mock
        Mockito.when(gameRepository.save(ArgumentMatchers.any(GameStatus.class))).
                then(AdditionalAnswers.returnsFirstArg());
        GameStatus gameStatus = TestUtil.createGameStatusForCaptureAllReward();
        Mockito.when(gameRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(gameStatus));

        //Operation
        int selectedPit = 4;
        int zeroPit = 2;
        int oppositePit = PitUtil.getOppositePitIndex(zeroPit);
        GameStatusOutput gameStatusOutput = gameManagerService.sow(gameId, selectedPit);

        //Assertion
        assertNotNull(gameStatusOutput, "gameStatusOutput should not be null");
        assertEquals(gameId, gameStatusOutput.getId(), "returned gameId is equal to passed gameId");
        assertEquals(gameStatusOutput.getActivePlayer(),
                PlayerType.PLAYER_2.name(),
                "the first player is active player again");
        assertEquals(MessageConstant.REWARD_CAPTURE_OPPOSITE_STONES.getMessage(),
                gameStatusOutput.getReward(), "the firstPlayer has a capture reward");
        assertNull(gameStatusOutput.getWinner(), "the winner is null");
        assertNotNull(gameStatusOutput.getPits(), "pits should not be null");
        assertEquals(gameStatusOutput.getPits().size(),
                GameConstant.ALL_PITS.getValue(),
                "the size of pits should be " + GameConstant.ALL_PITS.getValue());
        int stonesCount = gameStatusOutput.getPits().get(selectedPit).getStones();
        assertEquals(0, stonesCount, "selected pit has zero");
        stonesCount = gameStatusOutput.getPits().get(oppositePit).getStones();
        assertEquals(0, stonesCount, "opposite pit has zero");
        stonesCount = gameStatusOutput.getPits().get(zeroPit).getStones();
        assertEquals(0, stonesCount, "this pit capture all opposite pit");
        int rightBigPitNewStonesCount = gameStatusOutput.getPits().get(GameConstant.LEFT_BIG_PIT_INDEX.getValue()).getStones();
        assertEquals(6, rightBigPitNewStonesCount, "big pit capture all specific pits");
    }

    @Test
    @Order(2)
    void should_return_gameStatusOutput_with_playerOne_winner_when_sow() {
        //Mock
        Mockito.when(gameRepository.save(ArgumentMatchers.any(GameStatus.class))).
                then(AdditionalAnswers.returnsFirstArg());
        GameStatus gameStatus = TestUtil.createGameStatusForWiningPlayerOne();
        Mockito.when(gameRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(gameStatus));

        //Operation
        GameStatusOutput gameStatusOutput = gameManagerService.sow(gameId, 5);

        //Assertion
        assertNotNull(gameStatusOutput, "gameStatusOutput should not be null");
        assertEquals(gameId, gameStatusOutput.getId(), "returned gameId is equal to passed gameId");
        assertNull(gameStatusOutput.getReward(), "with no reward");
        assertEquals(PlayerType.PLAYER_1.toString(), gameStatusOutput.getWinner(), "with player one as a winner");
        assertEquals(PlayerType.PLAYER_1.toString(), gameStatusOutput.getActivePlayer(), "with no change to activePlayer");
        int playerOneStones = gameStatusOutput.getPits().get(GameConstant.LEFT_BIG_PIT_INDEX.getValue()).getStones();
        int playerTwoStones = gameStatusOutput.getPits().get(GameConstant.RIGHT_BIG_PIT_INDEX.getValue()).getStones();
        assertTrue(playerOneStones > playerTwoStones, "player one has more stones than player two");
        for (int i = 0; i < GameConstant.ALL_PITS.getValue(); i++) {
            if (i == GameConstant.LEFT_BIG_PIT_INDEX.getValue() || i == GameConstant.RIGHT_BIG_PIT_INDEX.getValue()) {
                continue;
            }
            int stonesCount = gameStatusOutput.getPits().get(i).getStones();
            assertEquals(0, stonesCount, "all small pits have zero stones");
        }
    }

    @Test
    @Order(2)
    void should_return_gameStatusOutput_with_playerTwo_winner_when_sow() {
        //Mock
        Mockito.when(gameRepository.save(ArgumentMatchers.any(GameStatus.class))).
                then(AdditionalAnswers.returnsFirstArg());
        GameStatus gameStatus = TestUtil.createGameStatusForWiningPlayerTwo();
        Mockito.when(gameRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(gameStatus));

        //Operation
        GameStatusOutput gameStatusOutput = gameManagerService.sow(gameId, 12);

        //Assertion
        assertNotNull(gameStatusOutput, "gameStatusOutput should not be null");
        assertEquals(gameId, gameStatusOutput.getId(), "returned gameId is equal to passed gameId");
        assertNull(gameStatusOutput.getReward(), "with no reward");
        assertEquals(PlayerType.PLAYER_2.toString(), gameStatusOutput.getWinner(), "with player two as a winner");
        assertEquals(PlayerType.PLAYER_2.toString(), gameStatusOutput.getActivePlayer(), "with no change to activePlayer");
        int playerOneStones = gameStatusOutput.getPits().get(GameConstant.LEFT_BIG_PIT_INDEX.getValue()).getStones();
        int playerTwoStones = gameStatusOutput.getPits().get(GameConstant.RIGHT_BIG_PIT_INDEX.getValue()).getStones();
        assertTrue(playerOneStones < playerTwoStones, "player two has more stones than player one");
        for (int i = 0; i < GameConstant.ALL_PITS.getValue(); i++) {
            if (i == GameConstant.LEFT_BIG_PIT_INDEX.getValue() || i == GameConstant.RIGHT_BIG_PIT_INDEX.getValue()) {
                continue;
            }
            int stonesCount = gameStatusOutput.getPits().get(i).getStones();
            assertEquals(0, stonesCount, "all small pits have zero stones");
        }
    }

    @Test
    @Order(2)
    void should_return_gameStatusOutput_with_draw_without_winner_when_sow() {
        //Mock
        Mockito.when(gameRepository.save(ArgumentMatchers.any(GameStatus.class))).
                then(AdditionalAnswers.returnsFirstArg());
        GameStatus gameStatus = TestUtil.createGameStatusForDraw();
        Mockito.when(gameRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(gameStatus));

        //Operation
        GameStatusOutput gameStatusOutput = gameManagerService.sow(gameId, 12);

        //Assertion
        assertNotNull(gameStatusOutput, "gameStatusOutput should not be null");
        assertEquals(gameId, gameStatusOutput.getId(), "returned gameId is equal to passed gameId");
        assertNull(gameStatusOutput.getReward(), "with no reward");
        assertEquals(PlayerType.NOBODY.toString(), gameStatusOutput.getWinner(), "with nobody as winner");
        assertEquals(PlayerType.PLAYER_2.toString(), gameStatusOutput.getActivePlayer(), "with no change to activePlayer");
        int playerOneStones = gameStatusOutput.getPits().get(GameConstant.LEFT_BIG_PIT_INDEX.getValue()).getStones();
        int playerTwoStones = gameStatusOutput.getPits().get(GameConstant.RIGHT_BIG_PIT_INDEX.getValue()).getStones();
        assertEquals(playerOneStones, playerTwoStones, "player two has equal stones than player one");
        for (int i = 0; i < GameConstant.ALL_PITS.getValue(); i++) {
            if (i == GameConstant.LEFT_BIG_PIT_INDEX.getValue() || i == GameConstant.RIGHT_BIG_PIT_INDEX.getValue()) {
                continue;
            }
            int stonesCount = gameStatusOutput.getPits().get(i).getStones();
            assertEquals(0, stonesCount, "all small pits have zero stones");
        }
    }

    @Test
    @Order(2)
    void should_return_gameStatusOutput_when_loadGame() {
        //Mock
        GameStatus gameStatus = TestUtil.createGameStatusForDraw();
        Mockito.when(gameRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(gameStatus));

        //Operation
        GameStatusOutput newGameStatusOutput = gameManagerService.loadGame(gameId);

        //Assertion
        assertNotNull(newGameStatusOutput, "gameStatus is not null");
        assertEquals(gameStatus.getId(), newGameStatusOutput.getId(), "new and old should be same");
        assertEquals(gameStatus.getActivePlayer().toString(), newGameStatusOutput.getActivePlayer(), "new and old should be same");
        assertEquals(gameStatus.getReward(), newGameStatusOutput.getReward(), "new and old should be same");
        assertNull(gameStatus.getWinner(), "winner is null");
        assertNull(newGameStatusOutput.getWinner(), "winner is null");
        assertEquals(gameStatus.getPits().size(), newGameStatusOutput.getPits().size(), "new and old should be same");
    }

    @Test
    @Order(2)
    void should_return_resourceNotFound_exception_when_loadGame() {
        //Mock
        Mockito.when(gameRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        //Operation
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> gameManagerService.loadGame(gameId),
                "ResourceNotFoundException was expected");

        //Assertion
        Assertions.assertEquals(String.format(MessageConstant.ENTITY_NOT_FOUND.getMessage(), gameId), thrown.getMessage());
    }

}
