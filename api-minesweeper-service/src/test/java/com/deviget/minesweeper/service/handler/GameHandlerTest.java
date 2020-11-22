package com.deviget.minesweeper.service.handler;

import com.deviget.minesweeper.service.exception.GameException;
import com.deviget.minesweeper.service.model.*;
import com.deviget.minesweeper.service.utils.ExceptionUtils;
import com.deviget.minesweeper.service.utils.TestUtils;
import com.deviget.minesweeper.service.validator.GameValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class GameHandlerTest {

    @Test
    @DisplayName("Flag cell successfully")
    public void handleFlagActionSuccessFully() {

        GameHandler gameHandler = new GameHandler(new GameValidator(Mockito.mock(ExceptionUtils.class)));

        Cell cells[][] = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = {{1, 2}, {4, 4}, {3, 0}};
        Board board = new Board(cells, minesCoordinates);

        Game game = new Game(board,"fooUserId");
        game.setHasMadeFirstMove(true);
        GameStatus status = gameHandler.handleAction(game, new GameMove(0, 0, GameAction.FLAG));
        Assertions.assertEquals(GameStatus.PLAYING, status);
        Assertions.assertEquals(cells[0][0].getState(), CellState.FLAGGED);

        cells[1][2].updateStatus(CellState.MARKED);
        status = gameHandler.handleAction(game, new GameMove(1, 2, GameAction.FLAG));
        Assertions.assertEquals(GameStatus.PLAYING, status);
        Assertions.assertEquals(cells[1][2].getState(), CellState.FLAGGED);
    }

    @Test
    @DisplayName("Mark cell successfully")
    public void handleMarkActionSuccessfully() {

        GameHandler gameHandler = new GameHandler(new GameValidator(Mockito.mock(ExceptionUtils.class)));

        Cell cells[][] = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = {{1, 2}, {4, 4}, {3, 0}};
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board,"fooUserId");
        game.setHasMadeFirstMove(true);
        GameStatus status = gameHandler.handleAction(game, new GameMove(0, 0, GameAction.MARK));
        Assertions.assertEquals(GameStatus.PLAYING, status);
        Assertions.assertEquals(cells[0][0].getState(), CellState.MARKED);

        cells[1][2].updateStatus(CellState.FLAGGED);
        status = gameHandler.handleAction(game, new GameMove(1, 2, GameAction.MARK));
        Assertions.assertEquals(GameStatus.PLAYING, status);
        Assertions.assertEquals(cells[1][2].getState(), CellState.MARKED);
    }

    @Test
    @DisplayName("Remove flag and mark successfully")
    public void handleRemoveFlagAndMarkSuccessfully() {

        GameHandler gameHandler = new GameHandler(new GameValidator(Mockito.mock(ExceptionUtils.class)));

        Cell cells[][] = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = {{1, 2}, {4, 4}, {3, 0}};
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board,"fooUserId");
        game.setHasMadeFirstMove(true);
        gameHandler.handleAction(game, new GameMove(0, 0, GameAction.FLAG));
        GameStatus flagRemovedStatus = gameHandler.handleAction(game, new GameMove(0, 0, GameAction.REMOVE_TAG));
        gameHandler.handleAction(game, new GameMove(2, 2, GameAction.MARK));
        GameStatus markRemovedStatus = gameHandler.handleAction(game, new GameMove(2, 2, GameAction.REMOVE_TAG));
        Assertions.assertEquals(GameStatus.PLAYING, flagRemovedStatus);
        Assertions.assertEquals(GameStatus.PLAYING, markRemovedStatus);
        Assertions.assertEquals(cells[0][0].getState(), CellState.CLOSED);
        Assertions.assertEquals(cells[2][2].getState(), CellState.CLOSED);
    }

    @Test
    @DisplayName("Remove flag should fail")
    public void handleRemoveFlagThrowException() throws IOException {

        final String TYPE = "ACTION_NOT_ALLOWED";
        final String DETAIL = "Only marked or flagged cells can be targeted";
        final int STATUS = 406;

        GameValidator gameValidator = Mockito.mock(GameValidator.class);
        Mockito.doThrow(new GameException(TYPE,DETAIL,STATUS))
                .when(gameValidator)
                .checkIfRemoveTagActionIsAllowed(Mockito.any(Cell.class));

        GameHandler gameHandler = new GameHandler(gameValidator);

        Cell cells[][] = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = {{1, 2}, {4, 4}, {3, 0}};
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board,"fooUserId");
        game.setHasMadeFirstMove(true);
        gameHandler.handleAction(game, new GameMove(0, 0, GameAction.FLIP));
        GameException gameException = Assertions.assertThrows(GameException.class, () -> {
            gameHandler.handleAction(game, new GameMove(0, 1, GameAction.REMOVE_TAG));
        });

    }


    @Test
    @DisplayName("Keep playing")
    public void handleFlipActionKeepPlaying() {

        GameHandler gameHandler = new GameHandler(new GameValidator(Mockito.mock(ExceptionUtils.class)));

        Cell cells[][] = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = {{1, 2}, {4, 4}, {3, 0}};
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board,"fooUserId");
        game.setHasMadeFirstMove(true);
        GameMove move = new GameMove(0, 0, GameAction.FLIP);
        GameStatus status = gameHandler.handleAction(game, move);
        Assertions.assertEquals(GameStatus.PLAYING, status);
        Assertions.assertEquals(cells[0][0].getState(), CellState.OPENED);

    }

    @Test
    @DisplayName("Win game")
    public void handleFlipActionWinGame() {

        GameHandler gameHandler = new GameHandler(new GameValidator(Mockito.mock(ExceptionUtils.class)));
        Cell cells[][] = TestUtils.buildCells(3, 3);
        int[][] minesCoordinates = {{2, 1}};
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board,"fooUserId");
        game.setHasMadeFirstMove(true);
        GameStatus status = gameHandler.handleAction(game, new GameMove(0, 1, GameAction.FLIP));
        GameStatus status2 = gameHandler.handleAction(game, new GameMove(2, 0, GameAction.FLIP));
        GameStatus status3 = gameHandler.handleAction(game, new GameMove(2, 2, GameAction.FLIP));
        Assertions.assertEquals(GameStatus.PLAYING, status);
        Assertions.assertEquals(GameStatus.PLAYING, status2);
        Assertions.assertEquals(GameStatus.WON, status3);

    }

    @Test
    @DisplayName("Lost game")
    public void handleFlipActionLostGame() {

        GameHandler gameHandler = new GameHandler(new GameValidator(Mockito.mock(ExceptionUtils.class)));
        Cell cells[][] = TestUtils.buildCells(3, 3);
        int[][] minesCoordinates = {{1, 1}, {2, 1}};
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board,"fooUserId");
        game.setHasMadeFirstMove(true);
        GameStatus status = gameHandler.handleAction(game, new GameMove(0, 1, GameAction.FLIP));
        GameStatus status2 = gameHandler.handleAction(game, new GameMove(2, 1, GameAction.FLIP));
        Assertions.assertEquals(GameStatus.PLAYING, status);
        Assertions.assertEquals(GameStatus.LOST, status2);
    }

}
