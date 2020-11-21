package com.deviget.minesweeper.service.service.impl;

import com.deviget.minesweeper.service.handler.GameHandler;
import com.deviget.minesweeper.service.model.Board;
import com.deviget.minesweeper.service.model.Game;
import com.deviget.minesweeper.service.model.GameMove;
import com.deviget.minesweeper.service.model.GameStatus;
import com.deviget.minesweeper.service.repository.GameRepository;
import com.deviget.minesweeper.service.service.GameService;
import com.deviget.minesweeper.service.validator.GameValidator;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository repository;
    private final GameHandler gameHandler;
    private final GameValidator gameValidator;


    public GameServiceImpl(GameRepository repository, GameHandler gameHandler, GameValidator gameValidator) {
        this.repository = repository;
        this.gameHandler = gameHandler;
        this.gameValidator = gameValidator;
    }

    @Override
    public Game createNewGame(int row, int columns, int mines, String userId) {
        gameValidator.checkIfGameCanBeCreated(row,columns,mines);
        Board board = new Board(row, columns, mines);
        Game game = new Game(board,userId);
        Game createdGame = repository.save(game);
        return createdGame;
    }

    @Override
    public Game makeMove(String gameId, GameMove move) {
        Optional<Game> optionalGame = repository.findById(gameId);
        Game game = gameValidator.checkIfGameExists(optionalGame);
        gameValidator.checkIfGameIsPlayable(game);
        gameValidator.checkIfItIsAValidCoordinate(move,game.getBoard());
        GameStatus status = gameHandler.handleAction(game, move);
        game.setStatus(status);
        game.setLastMove(Instant.now());
        return repository.save(game);
    }

    @Override
    public Optional<Game> findByGameId(String id) {
        return repository.findById(id);
    }
}
