package com.deviget.minesweeper.service.handler;

import com.deviget.minesweeper.service.model.*;
import com.deviget.minesweeper.service.validator.GameValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class GameHandler {

    private final GameValidator validator;
    private final Map<Boolean, BiFunction<Game, Cell, GameStatus>> flipAction;
    Logger logger = LoggerFactory.getLogger(GameHandler.class);

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
        logger.info("Flagging cell {}{}",cell.getRow(),cell.getColumn());
        cell.updateStatus(CellState.FLAGGED);
        return GameStatus.PLAYING;
    }

    private GameStatus markAction(Cell cell) {
        validator.checkIfMarkActionIsAllowed(cell);
        logger.info("Marking cell {}{}",cell.getRow(),cell.getColumn());
        cell.updateStatus(CellState.MARKED);
        return GameStatus.PLAYING;
    }

    private GameStatus removeTagAction(Cell cell) {
        validator.checkIfRemoveTagActionIsAllowed(cell);
        logger.info("Cleaning marks from cell {}{}",cell.getRow(),cell.getColumn());
        cell.updateStatus(CellState.CLOSED);
        return GameStatus.PLAYING;
    }


    private GameStatus flipActionFirstTime(Game game, Cell cell) {
        game.getBoard().initializeMines(cell.getRow(), cell.getColumn());
        game.setHasMadeFirstMove(true);
        logger.info("The first movement was made in the cell {}{}",cell.getRow(),cell.getColumn());
        return flipAction(game, cell);
    }


    private GameStatus flipAction(Game game, Cell cell) {
        validator.checkIfFlipActionIsAllowed(cell);
        Board board = game.getBoard();
        board.floodFlip(cell);
        boolean mineFound = board.getMines().get(0).getState().equals(CellState.OPENED);
        if (mineFound) {
            logger.info("User {} has lost",game.getUserId());
            return GameStatus.LOST;
        }
        int totalCells = board.getRows() * board.getColumns();
        int openedCells = board.getOpenedCells();
        int mines = board.getNumberOfMines();
        if (Math.addExact(openedCells, mines) == totalCells) {
            logger.info("User {} has won",game.getUserId());
            return GameStatus.WON;
        } else {
            return GameStatus.PLAYING;
        }
    }


}
