package com.deviget.minesweeper.service.service.impl;

import com.deviget.minesweeper.service.handler.GameHandler;
import com.deviget.minesweeper.service.model.*;
import com.deviget.minesweeper.service.repository.GameRepository;
import com.deviget.minesweeper.service.repository.UserRepository;
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
    private final UserRepository userRepository;

    public GameServiceImpl(GameRepository repository, GameHandler gameHandler,
                            GameValidator gameValidator, UserRepository userRepository) {
        this.repository = repository;
        this.gameHandler = gameHandler;
        this.gameValidator = gameValidator;
        this.userRepository = userRepository;
    }

    @Override
    public Game createNewGame(int row, int columns, int mines, String userName) {
        User user = gameValidator.checkIfUserExists(userRepository.findByUserName(userName));
        gameValidator.checkIfGameCanBeCreated(row, columns, mines);
        Board board = new Board(row, columns, mines);
        Game game = new Game(board, user.getId());
        Game createdGame = repository.save(game);
        return createdGame;
    }

    @Override
    public Game makeMove(String gameId, GameMove move, String userName) {
        User user = gameValidator.checkIfUserExists(userRepository.findByUserName(userName));
        Optional<Game> optionalGame = repository.findById(gameId);
        Game game = gameValidator.checkIfGameExists(optionalGame);
        gameValidator.checkIfGameAndUserMatches(game,user);
        gameValidator.checkIfGameIsPlayable(game);
        gameValidator.checkIfItIsAValidCoordinate(move, game.getBoard());
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
