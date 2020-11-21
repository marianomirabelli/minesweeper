package com.deviget.minesweeper.service.handler;

import com.deviget.minesweeper.service.exception.GameException;
import com.deviget.minesweeper.service.model.*;
import com.deviget.minesweeper.service.utils.ExceptionUtils;
import com.deviget.minesweeper.service.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class GameHandlerTest {

    @Test
    @DisplayName("Flag cell successfully")
    public void handleFlagActionSuccessFully() {

        GameHandler gameHandler = new GameHandler(Mockito.mock(ExceptionUtils.class));

        Cell cells[][] = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = {{1, 2}, {4, 4}, {3, 0}};
        Board board = new Board(cells, minesCoordinates);

        Game game = new Game(board);
        GameStatus status = gameHandler.handleAction(game, new GameMove(0, 0, GameAction.FLAG));
        Assertions.assertEquals(GameStatus.PLAYING, status);
        Assertions.assertEquals(cells[0][0].getState(), CellState.FLAGGED);

        cells[1][2].updateStatus(CellState.MARKED);
        status = gameHandler.handleAction(game, new GameMove(1, 2, GameAction.FLAG));
        Assertions.assertEquals(GameStatus.PLAYING, status);
        Assertions.assertEquals(cells[1][2].getState(), CellState.FLAGGED);
    }

    @Test
    @DisplayName("Flag flagged cell should fail")
    public void handleFlagActionThrowException() throws IOException {

        final String TYPE = "ACTION_NOT_ALLOWED";
        final String DETAIL = "Closed or marked cells can be flagged";
        final int STATUS = 406;

        GameHandler gameHandler = initHandler("game.action.flag.description");

        Cell cells[][] = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = {{1, 2}, {4, 4}, {3, 0}};
        Board board = new Board(cells, minesCoordinates);

        Game game = new Game(board);
        GameStatus status = gameHandler.handleAction(game, new GameMove(0, 0, GameAction.FLAG));
        Assertions.assertEquals(GameStatus.PLAYING, status);
        Assertions.assertEquals(cells[0][0].getState(), CellState.FLAGGED);

        GameException gameException = Assertions.assertThrows(GameException.class, () -> {
            cells[1][1].updateStatus(CellState.OPENED);
            gameHandler.handleAction(game, new GameMove(0, 0, GameAction.FLAG));
        });

        assertExceptionMessages(TYPE,DETAIL,STATUS,gameException);

    }


    @Test
    @DisplayName("Mark cell successfully")
    public void handleMarkActionSuccessfully() {

        GameHandler gameHandler = new GameHandler(Mockito.mock(ExceptionUtils.class));

        Cell cells[][] = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = {{1, 2}, {4, 4}, {3, 0}};
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board);

        GameStatus status = gameHandler.handleAction(game, new GameMove(0, 0, GameAction.MARK));
        Assertions.assertEquals(GameStatus.PLAYING, status);
        Assertions.assertEquals(cells[0][0].getState(), CellState.MARKED);

        cells[1][2].updateStatus(CellState.FLAGGED);
        status = gameHandler.handleAction(game, new GameMove(1, 2, GameAction.MARK));
        Assertions.assertEquals(GameStatus.PLAYING, status);
        Assertions.assertEquals(cells[1][2].getState(), CellState.MARKED);
    }

    @Test
    @DisplayName("Mark opened cell should fail")
    public void handleMarkActionThrowsException() throws IOException {

        final String TYPE = "ACTION_NOT_ALLOWED";
        final String DETAIL = "Closed or flagged cells can be marked";
        final int STATUS = 406;

        GameHandler gameHandler = initHandler("game.action.mark.description");

        Cell cells[][] = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = {{1, 2}, {4, 4}, {3, 0}};
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board);

        GameStatus status = gameHandler.handleAction(game, new GameMove(0, 0, GameAction.MARK));
        Assertions.assertEquals(GameStatus.PLAYING, status);
        Assertions.assertEquals(cells[0][0].getState(), CellState.MARKED);

        cells[1][1].updateStatus(CellState.OPENED);

        GameException gameException = Assertions.assertThrows(GameException.class, () -> {
            gameHandler.handleAction(game, new GameMove(1, 1, GameAction.MARK));
        });

        assertExceptionMessages(TYPE,DETAIL,STATUS,gameException);

    }

    @Test
    @DisplayName("Remove flag and mark successfully")
    public void handleRemoveFlagAndMarkSuccessfully() {

        GameHandler gameHandler = new GameHandler(Mockito.mock(ExceptionUtils.class));

        Cell cells[][] = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = {{1, 2}, {4, 4}, {3, 0}};
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board);

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
        final String DETAIL = "Marked or flagged cells can be targeted";
        final int STATUS = 406;

        GameHandler gameHandler = initHandler("game.action.remove.tag.description");

        Cell cells[][] = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = {{1, 2}, {4, 4}, {3, 0}};
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board);

        gameHandler.handleAction(game, new GameMove(0, 0, GameAction.FLIP));
        GameException gameException = Assertions.assertThrows(GameException.class, () -> {
            gameHandler.handleAction(game, new GameMove(0, 1, GameAction.REMOVE_TAG));
        });

        assertExceptionMessages(TYPE,DETAIL,STATUS,gameException);
    }


    @Test
    @DisplayName("Keep playing")
    public void handleFlipActionKeepPlaying() {

        GameHandler gameHandler = new GameHandler(Mockito.mock(ExceptionUtils.class));

        Cell cells[][] = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = {{1, 2}, {4, 4}, {3, 0}};
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board);

        GameMove move = new GameMove(0, 0, GameAction.FLIP);
        GameStatus status = gameHandler.handleAction(game, move);
        Assertions.assertEquals(GameStatus.PLAYING, status);
        Assertions.assertEquals(cells[0][0].getState(), CellState.OPENED);

    }

    @Test
    @DisplayName("Win game")
    public void handleFlipActionWinGame() {

        GameHandler gameHandler = new GameHandler(Mockito.mock(ExceptionUtils.class));
        Cell cells[][] = TestUtils.buildCells(3, 3);
        int[][] minesCoordinates = {{2, 1}};
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board);
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

        GameHandler gameHandler = new GameHandler(Mockito.mock(ExceptionUtils.class));
        Cell cells[][] = TestUtils.buildCells(3, 3);
        int[][] minesCoordinates = {{1, 1}, {2, 1}};
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board);
        GameStatus status = gameHandler.handleAction(game, new GameMove(0, 1, GameAction.FLIP));
        GameStatus status2 = gameHandler.handleAction(game, new GameMove(2, 1, GameAction.FLIP));
        Assertions.assertEquals(GameStatus.PLAYING, status);
        Assertions.assertEquals(GameStatus.LOST, status2);
    }

    @Test
    @DisplayName("flip action throws exception")
    public void handleFlipActionThrowException() throws IOException {

        final String TYPE = "ACTION_NOT_ALLOWED";
        final String DETAIL = "Closed or marked cells can be flipped";
        final int STATUS = 406;

        GameHandler gameHandler = initHandler("game.action.flip.description");
        Cell cells[][] = TestUtils.buildCells(3, 3);
        int[][] minesCoordinates = {{1, 1}};
        Board board = new Board(cells, minesCoordinates);
        Game game = new Game(board);
        gameHandler.handleAction(game, new GameMove(0, 1, GameAction.FLIP));
        GameException gameException = Assertions.assertThrows(GameException.class, () -> {
            gameHandler.handleAction(game, new GameMove(0, 1, GameAction.FLIP));
        });

        assertExceptionMessages(TYPE,DETAIL,STATUS,gameException);

    }


    private void assertExceptionMessages(String type, String detail, int status, GameException ex){
        Assertions.assertEquals(type.trim(), ex.getType().trim());
        Assertions.assertEquals(detail.trim(), ex.getDetail().trim());
        Assertions.assertEquals(status, ex.getStatus());
    }


    private GameHandler initHandler(String keyDetail) throws IOException {
        Properties properties = PropertiesLoaderUtils.loadAllProperties("test-messages.properties");

        ExceptionUtils exceptionUtils = Mockito.mock(ExceptionUtils.class);

        Mockito.when(exceptionUtils.buildException("game.action.general.type",
                    keyDetail,
                    "game.action.general.status"))
                .thenReturn(new GameException(
                            properties.getProperty("game.action.general.type"),
                            properties.getProperty(keyDetail),
                            Integer.parseInt(properties.getProperty("game.action.general.status"))));

        return new GameHandler(exceptionUtils);
    }


}
