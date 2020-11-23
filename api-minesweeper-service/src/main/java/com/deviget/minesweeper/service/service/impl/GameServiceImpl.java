package com.deviget.minesweeper.service.service.impl;

import com.deviget.minesweeper.service.handler.GameHandler;
import com.deviget.minesweeper.service.model.*;
import com.deviget.minesweeper.service.repository.GameRepository;
import com.deviget.minesweeper.service.repository.UserRepository;
import com.deviget.minesweeper.service.service.GameService;
import com.deviget.minesweeper.service.validator.GameValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository repository;
    private final GameHandler gameHandler;
    private final GameValidator gameValidator;
    private final UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    @Autowired
    public GameServiceImpl(GameRepository repository, GameHandler gameHandler,
                            GameValidator gameValidator, UserRepository userRepository) {
        this.repository = repository;
        this.gameHandler = gameHandler;
        this.gameValidator = gameValidator;
        this.userRepository = userRepository;
    }

    /**
     * This method is used to create new games. The user sets the number of rows, columns and mines required to build the board.
     * The maximum number of rows and columns allowed is 30, while the minimum is 3. On the other hand,
     * the number of mines has an upper limit equal to 20% of the squares on the board.
     * @param row
     * @param columns
     * @param mines
     * @param userName
     * @return
     */
    @Override
    public Game createNewGame(int row, int columns, int mines, String userName) {
        logger.info("Creating new game for user {}",userName);
        User user = gameValidator.checkIfUserExists(userRepository.findByUserName(userName));
        gameValidator.checkIfGameCanBeCreated(row, columns, mines);
        Board board = new Board(row, columns, mines);
        Game game = new Game(board, user.getId());
        Game createdGame = repository.save(game);
        return createdGame;
    }


    /**
     * This method is used to apply users moves. One user is only allowed to play with their associated games.
     * Additionally, only over games in PLAYABLE state actions are allowed.
     * @param gameId
     * @param move
     * @param userName
     * @return
     */
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
    /**
     * This method retrieves a game by id regardless on game status.
     * @param id
     * @return
     */
    @Override
    public Optional<Game> findByGameId(String id) {
        return repository.findById(id);
    }
}
