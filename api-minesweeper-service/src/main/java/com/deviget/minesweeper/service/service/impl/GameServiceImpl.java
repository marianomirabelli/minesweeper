package com.deviget.minesweeper.service.service.impl;

import com.deviget.minesweeper.service.handler.GameHandler;
import com.deviget.minesweeper.service.model.*;
import com.deviget.minesweeper.service.repository.GameRepository;
import com.deviget.minesweeper.service.service.GameService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository repository;
    private final GameHandler gameHandler;


    public GameServiceImpl(GameRepository repository, GameHandler gameHandler) {
        this.repository = repository;
        this.gameHandler = gameHandler;
    }

    @Override
    public Game createNewGame(int row, int columns, int mines) {

        Board board = new Board(row, columns, mines);
        Game game = new Game(board);
        Game createdGame = repository.save(game);
        return createdGame;
    }

    @Override
    public Game makeMove(String gameId, GameMove move) {
        Game game = repository.findById(gameId).get();
        gameHandler.handleAction(game,move);
        return repository.save(game);
    }

    @Override
    public Optional<Game> findByGameId(String id) {
        return repository.findById(id);
    }
}
