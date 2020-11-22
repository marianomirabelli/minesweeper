package com.deviget.minesweeper.service.validator;

import com.deviget.minesweeper.service.model.*;
import com.deviget.minesweeper.service.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GameValidator {

    private final ExceptionUtils exceptionUtils;

    @Autowired
    public GameValidator(ExceptionUtils exceptionUtils) {
        this.exceptionUtils = exceptionUtils;
    }

    public void checkIfGameIsPlayable(Game game) {
        if (game.getStatus().equals(GameStatus.LOST) ||
                game.getStatus().equals(GameStatus.WON)) {
            throw exceptionUtils.buildException("game.playable.type", "game.playable.description ", "game.playable.status");
        }
    }

    public Game checkIfGameExists(Optional<Game> opGame) {
        return opGame.orElseThrow(() ->
                exceptionUtils.buildException("game.not.found.type", "game.not.found.description", "game.not.found.status"));
    }

    public void checkIfItIsAValidCoordinate(GameMove move, Board board) {
        int row = move.getRow();
        int column = move.getColumn();
        if ((row <= 0 || row >= board.getRows()) ||
                (column <= 0 || column >= board.getColumns())) {
            throw exceptionUtils.buildException("game.coordinates.type", "game.coordinates.description", "game.coordinates.status");
        }
    }

    public void checkIfGameCanBeCreated(int rows, int columns, int mines) {
        int squares = rows * columns;
        int minesPercentage = (int) Math.ceil(squares * 0.20);
        if (mines > minesPercentage) {
            throw exceptionUtils.buildException("game.mines.exceeded.type", "game.mines.exceeded.description", "game.mines.exceeded.status");
        }
    }

    public void checkIfFlagActionIsAllowed(Cell cell) {
        if (cell.getState().equals(CellState.FLAGGED)
                || cell.getState().equals(CellState.OPENED)) {
            throw exceptionUtils.buildException("game.action.general.type", "game.action.flag.description", "game.action.general.status");
        }
    }

    public void checkIfMarkActionIsAllowed(Cell cell) {
        if (cell.getState().equals(CellState.MARKED)
                || cell.getState().equals(CellState.OPENED)) {
            throw exceptionUtils.buildException("game.action.general.type", "game.action.mark.description", "game.action.general.status");
        }
    }

    public void checkIfRemoveTagActionIsAllowed(Cell cell) {
        if (!cell.getState().equals(CellState.FLAGGED)
                && !cell.getState().equals(CellState.MARKED)) {
            throw exceptionUtils.buildException("game.action.general.type", "game.action.remove.tag.description", "game.action.general.status");
        }
    }

    public void checkIfFlipActionIsAllowed(Cell cell) {
        if (cell.getState().equals(CellState.FLAGGED)
                || cell.getState().equals(CellState.OPENED)) {
            throw exceptionUtils.buildException("game.action.general.type", "game.action.flip.description", "game.action.general.status");
        }
    }

}
