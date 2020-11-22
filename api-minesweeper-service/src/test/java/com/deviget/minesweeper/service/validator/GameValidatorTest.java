package com.deviget.minesweeper.service.validator;

import com.deviget.minesweeper.service.exception.GameException;
import com.deviget.minesweeper.service.model.*;
import com.deviget.minesweeper.service.utils.ExceptionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class GameValidatorTest {

    private ExceptionUtils exceptionUtils = Mockito.mock(ExceptionUtils.class);
    private GameValidator gameValidator = new GameValidator(exceptionUtils);

    @Test
    public void checkIfGameIsPlayable() {
        Game game = Mockito.mock(Game.class);
        GameException expectedException = new GameException("OPERATION_NOT_ALLOWED", "The game has finished", 406);
        Mockito.when(game.getStatus()).thenReturn(GameStatus.WON);
        Mockito.when(exceptionUtils.buildException("game.playable.type", "game.playable.description ",
                "game.playable.status")).thenReturn(expectedException);
        GameException exceptionThrown = Assertions.assertThrows(GameException.class,
                () -> gameValidator.checkIfGameIsPlayable(game));

        Assertions.assertEquals(expectedException, exceptionThrown);

    }

    @Test
    public void checkIfGameExists() {
        GameException expectedException = new GameException("GAME_NOT_FOUND", "The game does not exist", 404);
        Mockito.when(exceptionUtils
                .buildException("game.not.found.type", "game.not.found.description", "game.not.found.status"))
                .thenReturn(expectedException);
        GameException exceptionThrown = Assertions.assertThrows(GameException.class,
                () -> gameValidator.checkIfGameExists(Optional.empty()));

        Assertions.assertEquals(expectedException, exceptionThrown);

    }

    @Test
    public void checkIfItIsAValidCoordinate() {
        Board board = Mockito.mock(Board.class);
        Mockito.when(board.getRows()).thenReturn(5);
        Mockito.when(board.getColumns()).thenReturn(5);
        GameException expectedException = new GameException("INVALID_COORDINATES", "Coordinates are out of range", 412);

        Mockito.when(exceptionUtils
                .buildException("game.coordinates.type", "game.coordinates.description", "game.coordinates.status"))
                .thenReturn(expectedException);

        Assertions.assertThrows(GameException.class,
                () -> gameValidator.checkIfItIsAValidCoordinate(new GameMove(8, 9, GameAction.MARK), board));
        Assertions.assertThrows(GameException.class,
                () -> gameValidator.checkIfItIsAValidCoordinate(new GameMove(3, 9, GameAction.MARK), board));

        GameException exceptionThrown = Assertions.assertThrows(GameException.class,
                () -> gameValidator.checkIfItIsAValidCoordinate(new GameMove(8, 3, GameAction.MARK), board));
        Assertions.assertEquals(expectedException, exceptionThrown);
    }

    @Test
    public void checkIfGameCanBeCreated() {
        GameException expectedException = new GameException("MINES_NUMBER_EXCEEDED",
                "The number of mines exceeds the maximum allowed", 412);
        Mockito.when(exceptionUtils.buildException("game.mines.exceeded.type", "game.mines.exceeded.description",
                "game.mines.exceeded.status")).thenReturn(expectedException);
        GameException gameException = Assertions.assertThrows(GameException.class,
                () -> gameValidator.checkIfGameCanBeCreated(5, 5, 20));
        Assertions.assertEquals(expectedException, gameException);
    }

    @Test
    public void checkIfFlagActionIsAllowed() {
        Cell cell = new Cell(1, 1);
        cell.updateStatus(CellState.FLAGGED);
        Cell cell2 = new Cell(1, 2);
        cell2.updateStatus(CellState.OPENED);
        GameException expectedException = new GameException("OPERATION_NOT_ALLOWED", "Cell already flagged", 406);
        Mockito.when(exceptionUtils.buildException("game.action.general.type", "game.action.flag.description",
                "game.action.general.status")).thenReturn(expectedException);
        Assertions.assertThrows(GameException.class, () -> gameValidator.checkIfFlagActionIsAllowed(cell));
        GameException gameException = Assertions
                .assertThrows(GameException.class, () -> gameValidator.checkIfFlagActionIsAllowed(cell2));
        Assertions.assertEquals(expectedException, gameException);
    }

    @Test
    public void checkIfMarkActionIsAllowed() {
        Cell cell = new Cell(1, 1);
        cell.updateStatus(CellState.MARKED);
        Cell cell2 = new Cell(1, 2);
        cell2.updateStatus(CellState.OPENED);
        GameException expectedException = new GameException("OPERATION_NOT_ALLOWED", "Cell already marked", 406);
        Mockito.when(exceptionUtils.buildException("game.action.general.type", "game.action.mark.description",
                "game.action.general.status")).thenReturn(expectedException);
        Assertions.assertThrows(GameException.class, () -> gameValidator.checkIfMarkActionIsAllowed(cell));
        GameException gameException = Assertions
                .assertThrows(GameException.class, () -> gameValidator.checkIfMarkActionIsAllowed(cell2));
        Assertions.assertEquals(expectedException, gameException);
    }

    @Test
    public void checkIfRemoveTagActionIsAllowed() {
        Cell cell = new Cell(1, 1);
        cell.updateStatus(CellState.CLOSED);
        Cell cell2 = new Cell(1, 2);
        cell2.updateStatus(CellState.OPENED);
        GameException expectedException = new GameException("OPERATION_NOT_ALLOWED", "Cell already cleared", 406);
        Mockito.when(exceptionUtils.buildException("game.action.general.type", "game.action.remove.tag.description",
                "game.action.general.status")).thenReturn(expectedException);
        Assertions.assertThrows(GameException.class, () -> gameValidator.checkIfRemoveTagActionIsAllowed(cell));
        GameException gameException = Assertions
                .assertThrows(GameException.class, () -> gameValidator.checkIfRemoveTagActionIsAllowed(cell2));
        Assertions.assertEquals(expectedException, gameException);

    }

    @Test
    public void checkIfFlipActionIsAllowed() {
        Cell cell = new Cell(1, 1);
        cell.updateStatus(CellState.OPENED);
        Cell cell2 = new Cell(1, 2);
        cell2.updateStatus(CellState.FLAGGED);
        GameException expectedException = new GameException("OPERATION_NOT_ALLOWED", "Cell already opened", 406);
        Mockito.when(exceptionUtils.buildException("game.action.general.type", "game.action.flip.description",
                "game.action.general.status")).thenReturn(expectedException);
        Assertions.assertThrows(GameException.class, () -> gameValidator.checkIfFlipActionIsAllowed(cell));
        GameException gameException = Assertions
                .assertThrows(GameException.class, () -> gameValidator.checkIfFlipActionIsAllowed(cell2));
        Assertions.assertEquals(expectedException, gameException);

    }
}
