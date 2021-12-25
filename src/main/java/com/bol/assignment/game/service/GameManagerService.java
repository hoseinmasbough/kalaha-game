package com.bol.assignment.game.service;

import com.bol.assignment.game.common.GameSpecifications;
import com.bol.assignment.game.common.MessageConstant;
import com.bol.assignment.game.entity.GameStatus;
import com.bol.assignment.game.exception.ResourceNotFoundException;
import com.bol.assignment.game.mapper.GameStatusMapper;
import com.bol.assignment.game.dto.GameStatusOutput;
import com.bol.assignment.game.core.GameHandler;
import com.bol.assignment.game.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameManagerService {

    private final GameSpecifications gameSpecifications;
    private final GameRepository gameRepository;
    private final GameValidation gameValidation;

    public GameManagerService(GameSpecifications gameSpecifications, GameRepository gameRepository, GameValidation gameValidation) {
        this.gameSpecifications = gameSpecifications;
        this.gameRepository = gameRepository;
        this.gameValidation = gameValidation;
    }

    public GameStatusOutput createGame() {
        GameStatus gameStatus = GameHandler.createGame(gameSpecifications.getStones());
        gameStatus = gameRepository.save(gameStatus);
        return GameStatusMapper.INSTANCE.entityToOutput(gameStatus);
    }

    public GameStatusOutput sow(Long gameId, Integer pitId) {
        GameStatus gameStatus = findGameById(gameId);
        gameValidation.validateSelectedPit(gameStatus.getActivePlayer(), pitId);

        GameHandler gameHandler = new GameHandler(gameStatus, pitId);
        gameHandler.sow();
        boolean gameShouldBeFinished = gameHandler.isGameFinished();
        if (gameShouldBeFinished) {
            gameHandler.finishGame();
        } else {
            log.trace("No, let's continue.");
            gameHandler.checkPlayerTurn();
        }
        gameStatus = gameHandler.getGameStatus();
        gameRepository.save(gameStatus);
        return GameStatusMapper.INSTANCE.entityToOutput(gameStatus);
    }

    public GameStatusOutput loadGame(Long id) throws ResourceNotFoundException {
        GameStatus gameStatus = findGameById(id);
        return GameStatusMapper.INSTANCE.entityToOutput(gameStatus);
    }

    private GameStatus findGameById(Long id){
        return gameRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format(MessageConstant.ENTITY_NOT_FOUND.getMessage(), id)));
    }
}