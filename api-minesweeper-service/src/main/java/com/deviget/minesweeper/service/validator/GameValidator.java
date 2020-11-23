package com.deviget.minesweeper.service.validator;

import com.deviget.minesweeper.service.model.*;
import com.deviget.minesweeper.service.utils.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GameValidator {

    private final ExceptionUtils exceptionUtils;

    Logger logger = LoggerFactory.getLogger(GameValidator.class);

    @Autowired
    public GameValidator(ExceptionUtils exceptionUtils) {
        this.exceptionUtils = exceptionUtils;
    }

    /**
     * This method check if a game is in PLAYABLE state. If not this throw an exception
     * o avoid modifications to finished games.
     * @param game
     */
    public void checkIfGameIsPlayable(Game game) {
        if (game.getStatus().equals(GameStatus.LOST) ||
                game.getStatus().equals(GameStatus.WON)) {
            logger.warn("The game {} is in {} state and is not playable anymore",game.getId(),game.getStatus());
            throw exceptionUtils
                    .buildException("game.playable.type", "game.playable.description", "game.playable.status");
        }
    }

    /**
     * This method checks whether the received user is null or not. If the user is null, an exception is thrown.
     * @param user
     * @return
     */
    public User checkIfUserExists(Optional<User> user) {
        return user.orElseThrow(() ->
                exceptionUtils
                .buildException("user.not.existent.type", "user.not.existent.description","user.not.existent.status"));
    }

    /**
     * This method checks whether the received game is null or not. If the game is null, an exception is thrown.
     * @param opGame
     * @return
     */
    public Game checkIfGameExists(Optional<Game> opGame) {
        return opGame.orElseThrow(() ->
                exceptionUtils
                        .buildException("game.not.found.type", "game.not.found.description", "game.not.found.status"));
    }

    /**
     * This method validates that a user can only modify a game associated with it
     * @param game
     * @param user
     */
    public void checkIfGameAndUserMatches(Game game, User user) {
        if(!game.getUserId().equals(user.getId())){
            logger.warn("The game {} does not allow to user {}",game.getId(),user.getId());
            throw exceptionUtils
                    .buildException("user.game.not.match.type", "user.game.not.match.description", "user.game.not.match.status");
        }
    }

    /**
     * This method checks if the coordinate received is a valid coordinate inside the board.
     * @param move
     * @param board
     */
    public void checkIfItIsAValidCoordinate(GameMove move, Board board) {
        int row = move.getRow();
        int column = move.getColumn();
        if ((row < 0 || row >= board.getRows()) ||
                (column < 0 || column >= board.getColumns())) {
            logger.warn("{} {} coordinate is not valid",row,column);
            throw exceptionUtils
                    .buildException("game.coordinates.type", "game.coordinates.description", "game.coordinates.status");
        }
    }

    /**
     This method validates the upper and lower limits of rows and columns allowed to build a new board.
     Additionally, this method validates the number of mines that have an upper limit
     that is set as 20% of the number of squares on the board.
     * @param rows
     * @param columns
     * @param mines
     */
    public void checkIfGameCanBeCreated(int rows, int columns, int mines) {
        int squares = rows * columns;
        int minesPercentage = (int) Math.ceil(squares * 0.20);
        if (mines<=0 || mines > minesPercentage) {
            logger.warn("{} number of mines is out of range",mines);
            throw exceptionUtils.buildException("game.mines.exceeded.type", "game.mines.exceeded.description",
                    "game.mines.exceeded.status");
        }
        if((rows < 3 || columns <3) || (rows>30 || columns>30)){
            logger.warn("{} {} board size is out of allowed range",rows,columns);
            throw exceptionUtils.buildException("game.row.columns.type", "game.row.columns.description",
                    "game.row.columns.status");
        }
    }
    /**
     * This method checks if a FLAG action is allowed.
     * To achieve this, the cell must not be in the FLAGGED or OPENED state
     * @param cell
     */
    public void checkIfFlagActionIsAllowed(Cell cell) {
        if (cell.getState().equals(CellState.FLAGGED)
                || cell.getState().equals(CellState.OPENED)) {
            logger.warn("Flag action is not allowed at cell {} {}",cell.getRow(),cell.getColumn());
            throw exceptionUtils.buildException("game.action.general.type", "game.action.flag.description",
                    "game.action.general.status");
        }
    }

    /**
     * This method checks if a MARK action is allowed.
     * To achieve this, the cell must not be in the MARKED or OPENED state
     * @param cell
     */
    public void checkIfMarkActionIsAllowed(Cell cell) {
        if (cell.getState().equals(CellState.MARKED)
                || cell.getState().equals(CellState.OPENED)) {
            logger.warn("Mark action is not allowed at cell {} {}",cell.getRow(),cell.getColumn());
            throw exceptionUtils.buildException("game.action.general.type", "game.action.mark.description",
                    "game.action.general.status");
        }
    }

    /**
     * This method checks if a REMOVE_TAGS action is allowed.
     * To achieve this, the cell must be in the MARKED or FLAGGED state
     * @param cell
     */
    public void checkIfRemoveTagActionIsAllowed(Cell cell) {
        if (!cell.getState().equals(CellState.FLAGGED)
                && !cell.getState().equals(CellState.MARKED)) {
            logger.warn("Remove tags action is not allowed at cell {} {}",cell.getRow(),cell.getColumn());
            throw exceptionUtils.buildException("game.action.general.type", "game.action.remove.tag.description",
                    "game.action.general.status");
        }
    }

    /**
     * This method checks if a FLIP action is allowed.
     * To achieve this, the cell must be in the FLAGGED or CLOSED state
     * @param cell
     */
    public void checkIfFlipActionIsAllowed(Cell cell) {
        if (cell.getState().equals(CellState.FLAGGED)
                || cell.getState().equals(CellState.OPENED)) {
            logger.warn("Flip action is not allowed at cell {} {}",cell.getRow(),cell.getColumn());
            throw exceptionUtils.buildException("game.action.general.type", "game.action.flip.description",
                    "game.action.general.status");
        }
    }

}
