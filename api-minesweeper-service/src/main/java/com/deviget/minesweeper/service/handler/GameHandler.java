package com.deviget.minesweeper.service.handler;

import com.deviget.minesweeper.service.model.*;
import com.deviget.minesweeper.service.utils.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class GameHandler {

    private final ExceptionUtils exceptionUtils;
    private Map<Boolean, BiFunction<Game,Cell,GameStatus>> flipAction;

    public GameHandler(ExceptionUtils exceptionUtils) {
        this.exceptionUtils = exceptionUtils;
        this.flipAction = new HashMap<>();
        this.flipAction.put(Boolean.FALSE,(g,c)->flipActionFirstTime(g,c));
        this.flipAction.put(Boolean.TRUE,(g,c)->flipAction(g,c));
    }

    public GameStatus handleAction(Game game, GameMove move) {

        Board board = game.getBoard();
        Cell cell = board.getCells()[move.getRow()][move.getColumn()];
        GameStatus status = switch (move.getAction()) {
            case FLAG -> flagAction(cell);
            case MARK -> markAction(cell);
            case REMOVE_TAG -> removeTagAction(cell);
            case FLIP -> this.flipAction.get(game.hasMadeFirstMove()).apply(game,cell);
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

    private GameStatus removeTagAction(Cell cell){
        if (cell.getState().equals(CellState.FLAGGED)
            || cell.getState().equals(CellState.MARKED)){

            cell.updateStatus(CellState.CLOSED);
            return GameStatus.PLAYING;
        }
        throw exceptionUtils.buildException("game.action.general.type", "game.action.remove.tag.description", "game.action.general.status");
    }


    private GameStatus flipActionFirstTime(Game game, Cell cell){
        game.getBoard().initializeMines(cell.getRow(),cell.getColumn());
        game.setHasMadeFirstMove(true);
        return flipAction(game,cell);
    }


    private GameStatus flipAction(Game game, Cell cell) {
        Board board = game.getBoard();
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
