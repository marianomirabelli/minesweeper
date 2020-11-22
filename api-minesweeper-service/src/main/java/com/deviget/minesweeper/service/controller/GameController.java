
package com.deviget.minesweeper.service.controller;

import com.deviget.minesweeper.api.GameDTO;
import com.deviget.minesweeper.api.GameMoveDTO;
import com.deviget.minesweeper.api.StartGameDTO;
import com.deviget.minesweeper.service.model.Game;
import com.deviget.minesweeper.service.model.GameMove;
import com.deviget.minesweeper.service.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
@Api(value="/game",tags={"Game Controller"})
public class GameController {

    private final GameService service;
    private final DefaultConversionService conversionService;

    @Autowired
    public GameController(GameService service, DefaultConversionService conversionService) {
        this.service = service;
        this.conversionService = conversionService;
    }

    @ApiOperation(value = "startNewGame" ,notes = "Creates a new game",response = GameDTO.class)
    @PostMapping
    public ResponseEntity<GameDTO> startNewGame(@RequestHeader("X-UserId") String userId,
                                                @RequestBody StartGameDTO action) {
        Game game = service.createNewGame(action.getRows(), action.getColumns(), action.getMines(),userId);
        GameDTO gameDTO = conversionService.convert(game, GameDTO.class);
        ResponseEntity<GameDTO> responseEntity = new ResponseEntity(gameDTO, HttpStatus.CREATED);
        return responseEntity;
    }

    @ApiOperation(value = "doAction" ,notes = "Execute an action in the game",response = GameDTO.class)
    @PatchMapping("{id}")
    public ResponseEntity<GameDTO> playGame(@RequestHeader("X-UserId") String userId,
                                            @PathVariable String id,
                                            @RequestBody GameMoveDTO action) {
        GameMove gameMove = conversionService.convert(action,GameMove.class);
        Game game = service.makeMove(id,gameMove);
        GameDTO gameDTO = conversionService.convert(game, GameDTO.class);
        ResponseEntity<GameDTO> responseEntity = new ResponseEntity(gameDTO, HttpStatus.OK);
        return responseEntity;
    }

    @ApiOperation(value = "retrieveGame" ,notes = "Retrieves a game by id",response = GameDTO.class)
    @GetMapping("{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable String id) {
        ResponseEntity responseEntity = service.findByGameId(id).map(g -> {
            GameDTO gameDto = conversionService.convert(g, GameDTO.class);
            return new ResponseEntity(gameDto, HttpStatus.OK);
        }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
        return responseEntity;
    }


}
