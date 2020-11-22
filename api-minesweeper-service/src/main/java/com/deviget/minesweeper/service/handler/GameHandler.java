package com.deviget.minesweeper.service.handler;

import com.deviget.minesweeper.service.model.*;
import com.deviget.minesweeper.service.utils.ExceptionUtils;
import com.deviget.minesweeper.service.validator.GameValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class GameHandler {

    private final GameValidator validator;
    private final Map<Boolean, BiFunction<Game, Cell, GameStatus>> flipAction;

    public GameHandler(GameValidator validator) {
        this.validator = validator;
        this.flipAction = new HashMap<>();
        this.flipAction.put(Boolean.FALSE, (g, c) -> flipActionFirstTime(g, c));
        this.flipAction.put(Boolean.TRUE, (g, c) -> flipAction(g, c));
    }

    public GameStatus handleAction(Game game, GameMove move) {

        Board board = game.getBoard();
        Cell cell = board.getCells()[move.getRow()][move.getColumn()];
        GameStatus status = switch (move.getAction()) {
            case FLAG -> flagAction(cell);
            case MARK -> markAction(cell);
            case REMOVE_TAG -> removeTagAction(cell);
            case FLIP -> this.flipAction.get(game.hasMadeFirstMove()).apply(game, cell);
        };
        return status;
    }

    private GameStatus flagAction(Cell cell) {
        validator.checkIfFlagActionIsAllowed(cell);
        cell.updateStatus(CellState.FLAGGED);
        return GameStatus.PLAYING;
    }

    private GameStatus markAction(Cell cell) {
        validator.checkIfMarkActionIsAllowed(cell);
        cell.updateStatus(CellState.MARKED);
        return GameStatus.PLAYING;
    }

    private GameStatus removeTagAction(Cell cell) {
        validator.checkIfRemoveTagActionIsAllowed(cell);
        cell.updateStatus(CellState.CLOSED);
        return GameStatus.PLAYING;
    }


    private GameStatus flipActionFirstTime(Game game, Cell cell) {
        game.getBoard().initializeMines(cell.getRow(), cell.getColumn());
        game.setHasMadeFirstMove(true);
        return flipAction(game, cell);
    }


    private GameStatus flipAction(Game game, Cell cell) {
        validator.checkIfFlipActionIsAllowed(cell);
        Board board = game.getBoard();
        board.floodFlip(cell);
        boolean mineFound = board.getMines().get(0).getState().equals(CellState.OPENED);
        if (mineFound) {
            return GameStatus.LOST;
        }
        int totalCells = board.getRows() * board.getColumns();
        int openedCells = board.getOpenedCells();
        int mines = board.getNumberOfMines();
        if (Math.addExact(openedCells, mines) == totalCells) {
            return GameStatus.WON;
        } else {
            return GameStatus.PLAYING;
        }
    }


}
