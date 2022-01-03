package com.bol.assignment.game.controller;

import com.bol.assignment.game.dto.GameStatusOutput;
import com.bol.assignment.game.service.GameManagerService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/kalaha")
@Api(value = "Kalaha Game API. Set of endpoints for Creating, Sowing and Checking status of the Game",
        tags = {"kalaha"})
public class GameController {

    private final GameManagerService gameManagerService;

    public GameController(GameManagerService gameManagerService) {
        this.gameManagerService = gameManagerService;
    }

    @PostMapping
    @ApiOperation(value = "Creating a new Kalaha game instance. It returns a GameStatus object with unique gameId that is used for playing the game",
            tags = {"kalaha"}, produces = "Application/JSON", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Game created", response = GameStatusOutput.class)})
    public ResponseEntity<GameStatusOutput> createGame() {
        GameStatusOutput game = gameManagerService.createGame();
        log.info("game is created. id:{}", game.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }

    @PutMapping(value = "{gameId}/pits/{pitId}")
    @ApiOperation(value = "Player sowing one of the valid pit. It keeps the history of the Game instance for consecutive requests.",
            tags = {"kalaha"}, produces = "Application/JSON", httpMethod = "PUT")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = GameStatusOutput.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Game not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    public ResponseEntity<GameStatusOutput> selectPit(
            @ApiParam(name = "gameId",
                    value = "The gameId is created by calling createGame() method. Cannot be empty",
                    required = true,
                    example = "1")
            @PathVariable(value = "gameId") Long gameId,
            @ApiParam(name = "pitId",
                    value = "The index of selected pit. A numerical range between 0 to 12 except 6. Cannot be empty",
                    required = true,
                    example = "0")
            @PathVariable(value = "pitId") Integer pitId) {
        log.info("sow is called. gameId:{}, pitIndex:{}", gameId, pitId);
        GameStatusOutput gameStatusOutput = gameManagerService.sow(gameId, pitId);
        return new ResponseEntity<>(gameStatusOutput, HttpStatus.OK);
    }

    @GetMapping("{gameId}")
    @ApiOperation(value = "Get gameStatus by gameId", notes = "Returns the latest status of the specified game by id",
            tags = {"kalaha"}, httpMethod = "GET", produces = "Application/JSON")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = GameStatusOutput.class),
            @ApiResponse(code = 404, message = "Game not found")})
    public ResponseEntity<GameStatusOutput> loadGame(
            @ApiParam(name = "gameId",
                    value = "The id of game created by calling createGame() method. Cannot be empty",
                    required = true,
                    example = "1")
            @PathVariable(value = "gameId") Long gameId) {
        log.debug("findGameById is called. gameId:{}", gameId);
        return ResponseEntity.ok(gameManagerService.loadGame(gameId));
    }
}
