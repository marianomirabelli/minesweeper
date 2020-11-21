
package com.deviget.minesweeper.service.controller;

import com.deviget.minesweeper.api.GameDTO;
import com.deviget.minesweeper.api.StartGameAction;
import com.deviget.minesweeper.service.model.Game;
import com.deviget.minesweeper.service.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("game")
public class GameController {

    private final GameService service;
    private final DefaultConversionService conversionService;

    @Autowired
    public GameController(GameService service, DefaultConversionService conversionService) {
        this.service = service;
        this.conversionService = conversionService;
    }

    @PostMapping
    public ResponseEntity<GameDTO> startNewGame(@RequestBody StartGameAction action) {
        Game game = service.createNewGame(action.getRows(), action.getColumns(), action.getMines());
        GameDTO gameDTO = conversionService.convert(game, GameDTO.class);
        ResponseEntity<GameDTO> responseEntity = new ResponseEntity(gameDTO, HttpStatus.CREATED);
        return responseEntity;
    }

    @GetMapping("{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable String id) {
        ResponseEntity responseEntity = service.findByGameId(id).map(g -> {
            GameDTO gameDto = conversionService.convert(g, GameDTO.class);
            return new ResponseEntity(gameDto, HttpStatus.OK);
        }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
        return responseEntity;
    }


}