package com.deviget.minesweeper.service.handler;

import com.deviget.minesweeper.service.model.*;
import org.springframework.stereotype.Component;

@Component
public class GameHandler {

    public void handleAction(Game game, GameMove move) {

        Board board = game.getBoard();
        Cell cell = board.getCells()[move.getRow()][move.getColumn()];
        switch (move.getAction()) {
            case FLAG -> flagAction(cell);
            case MARK -> markAction(cell);
            case FLIP -> flipAction(board, cell);
        }
    }

    private void flagAction(Cell cell) {
        if (!cell.getState().equals(CellState.FLAGGED)
                && cell.getState().equals(CellState.CLOSED)) {

            cell.updateStatus(CellState.FLAGGED);

        }
    }

    private void markAction(Cell cell) {
        if (!cell.getState().equals(CellState.MARKED)
                && cell.getState().equals(CellState.CLOSED)) {

            cell.updateStatus(CellState.MARKED);

        }
    }

    private void flipAction(Board board, Cell cell) {
        if (!cell.getState().equals(CellState.FLAGGED)
                && cell.getState().equals(CellState.CLOSED)) {
            board.floodFlip(cell);
        }
    }

}
