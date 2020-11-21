package com.deviget.minesweeper.service.handler;

import com.deviget.minesweeper.service.model.*;
import com.deviget.minesweeper.service.utils.ExceptionUtils;
import org.springframework.stereotype.Component;

@Component
public class GameHandler {

    private final ExceptionUtils exceptionUtils;

    public GameHandler(ExceptionUtils exceptionUtils) {
        this.exceptionUtils = exceptionUtils;
    }

    public GameStatus handleAction(Game game, GameMove move) {

        Board board = game.getBoard();
        Cell cell = board.getCells()[move.getRow()][move.getColumn()];
        GameStatus status = switch (move.getAction()) {
            case FLAG -> flagAction(cell);
            case MARK -> markAction(cell);
            case FLIP -> flipAction(board, cell);
        };
        return status;
    }

    private GameStatus flagAction(Cell cell) {
        if (!cell.getState().equals(CellState.FLAGGED)
                && !cell.getState().equals(CellState.OPENED)) {

            cell.updateStatus(CellState.FLAGGED);
            return GameStatus.PLAYING;

        }
        throw exceptionUtils.buildException("game.action.general.type", "game.action.flag.description", "game.action.general.status");
    }

    private GameStatus markAction(Cell cell) {
        if (!cell.getState().equals(CellState.MARKED)
                && !cell.getState().equals(CellState.OPENED)) {

            cell.updateStatus(CellState.MARKED);
            return GameStatus.PLAYING;

        }
        throw exceptionUtils.buildException("game.action.general.type", "game.action.mark.description", "game.action.general.status");
    }

    private GameStatus flipAction(Board board, Cell cell) {
        if (!cell.getState().equals(CellState.FLAGGED)
                && !cell.getState().equals(CellState.OPENED)) {
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
        throw exceptionUtils.buildException("game.action.general.type", "game.action.flip.description", "game.action.general.status");
    }


}
