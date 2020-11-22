package com.deviget.minesweeper.service.validator;

import com.deviget.minesweeper.service.exception.MinesweeperException;
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
        MinesweeperException expectedException = new MinesweeperException("OPERATION_NOT_ALLOWED",
                "The game has finished", 406);
        Mockito.when(game.getStatus()).thenReturn(GameStatus.WON);
        Mockito.when(exceptionUtils.buildException("game.playable.type", "game.playable.description ",
                "game.playable.status")).thenReturn(expectedException);
        MinesweeperException exceptionThrown = Assertions.assertThrows(MinesweeperException.class,
                () -> gameValidator.checkIfGameIsPlayable(game));

        Assertions.assertEquals(expectedException, exceptionThrown);

    }

    @Test
    public void checkIfGameExists() {
        MinesweeperException expectedException = new MinesweeperException("GAME_NOT_FOUND", "The game does not exist",
                404);
        Mockito.when(exceptionUtils
                .buildException("game.not.found.type", "game.not.found.description", "game.not.found.status"))
                .thenReturn(expectedException);
        MinesweeperException exceptionThrown = Assertions.assertThrows(MinesweeperException.class,
                () -> gameValidator.checkIfGameExists(Optional.empty()));

        Assertions.assertEquals(expectedException, exceptionThrown);

    }

    @Test
    public void checkIfItIsAValidCoordinate() {
        Board board = Mockito.mock(Board.class);
        Mockito.when(board.getRows()).thenReturn(5);
        Mockito.when(board.getColumns()).thenReturn(5);
        MinesweeperException expectedException = new MinesweeperException("INVALID_COORDINATES",
                "Coordinates are out of range", 412);

        Mockito.when(exceptionUtils
                .buildException("game.coordinates.type", "game.coordinates.description", "game.coordinates.status"))
                .thenReturn(expectedException);

        Assertions.assertThrows(MinesweeperException.class,
                () -> gameValidator.checkIfItIsAValidCoordinate(new GameMove(8, 9, GameAction.MARK), board));
        Assertions.assertThrows(MinesweeperException.class,
                () -> gameValidator.checkIfItIsAValidCoordinate(new GameMove(3, 9, GameAction.MARK), board));

        MinesweeperException exceptionThrown = Assertions.assertThrows(MinesweeperException.class,
                () -> gameValidator.checkIfItIsAValidCoordinate(new GameMove(8, 3, GameAction.MARK), board));
        Assertions.assertEquals(expectedException, exceptionThrown);
    }

    @Test
    public void checkIfGameCanBeCreated() {
        MinesweeperException expectedException = new MinesweeperException("MINES_NUMBER_EXCEEDED",
                "The number of mines exceeds the maximum allowed", 412);
        Mockito.when(exceptionUtils.buildException("game.mines.exceeded.type", "game.mines.exceeded.description",
                "game.mines.exceeded.status")).thenReturn(expectedException);
        MinesweeperException minesWeeperException = Assertions.assertThrows(MinesweeperException.class,
                () -> gameValidator.checkIfGameCanBeCreated(5, 5, 20));
        Assertions.assertEquals(expectedException, minesWeeperException);
    }

    @Test
    public void checkIfFlagActionIsAllowed() {
        Cell cell = new Cell(1, 1);
        cell.updateStatus(CellState.FLAGGED);
        Cell cell2 = new Cell(1, 2);
        cell2.updateStatus(CellState.OPENED);
        MinesweeperException expectedException = new MinesweeperException("OPERATION_NOT_ALLOWED",
                "Cell already flagged", 406);
        Mockito.when(exceptionUtils.buildException("game.action.general.type", "game.action.flag.description",
                "game.action.general.status")).thenReturn(expectedException);
        Assertions.assertThrows(MinesweeperException.class, () -> gameValidator.checkIfFlagActionIsAllowed(cell));
        MinesweeperException minesWeeperException = Assertions
                .assertThrows(MinesweeperException.class, () -> gameValidator.checkIfFlagActionIsAllowed(cell2));
        Assertions.assertEquals(expectedException, minesWeeperException);
    }

    @Test
    public void checkIfMarkActionIsAllowed() {
        Cell cell = new Cell(1, 1);
        cell.updateStatus(CellState.MARKED);
        Cell cell2 = new Cell(1, 2);
        cell2.updateStatus(CellState.OPENED);
        MinesweeperException expectedException = new MinesweeperException("OPERATION_NOT_ALLOWED",
                "Cell already marked", 406);
        Mockito.when(exceptionUtils.buildException("game.action.general.type", "game.action.mark.description",
                "game.action.general.status")).thenReturn(expectedException);
        Assertions.assertThrows(MinesweeperException.class, () -> gameValidator.checkIfMarkActionIsAllowed(cell));
        MinesweeperException minesWeeperException = Assertions
                .assertThrows(MinesweeperException.class, () -> gameValidator.checkIfMarkActionIsAllowed(cell2));
        Assertions.assertEquals(expectedException, minesWeeperException);
    }

    @Test
    public void checkIfRemoveTagActionIsAllowed() {
        Cell cell = new Cell(1, 1);
        cell.updateStatus(CellState.CLOSED);
        Cell cell2 = new Cell(1, 2);
        cell2.updateStatus(CellState.OPENED);
        MinesweeperException expectedException = new MinesweeperException("OPERATION_NOT_ALLOWED",
                "Cell already cleared", 406);
        Mockito.when(exceptionUtils.buildException("game.action.general.type", "game.action.remove.tag.description",
                "game.action.general.status")).thenReturn(expectedException);
        Assertions.assertThrows(MinesweeperException.class, () -> gameValidator.checkIfRemoveTagActionIsAllowed(cell));
        MinesweeperException minesWeeperException = Assertions
                .assertThrows(MinesweeperException.class, () -> gameValidator.checkIfRemoveTagActionIsAllowed(cell2));
        Assertions.assertEquals(expectedException, minesWeeperException);

    }

    @Test
    public void checkIfFlipActionIsAllowed() {
        Cell cell = new Cell(1, 1);
        cell.updateStatus(CellState.OPENED);
        Cell cell2 = new Cell(1, 2);
        cell2.updateStatus(CellState.FLAGGED);
        MinesweeperException expectedException = new MinesweeperException("OPERATION_NOT_ALLOWED",
                "Cell already opened", 406);
        Mockito.when(exceptionUtils.buildException("game.action.general.type", "game.action.flip.description",
                "game.action.general.status")).thenReturn(expectedException);
        Assertions.assertThrows(MinesweeperException.class, () -> gameValidator.checkIfFlipActionIsAllowed(cell));
        MinesweeperException minesWeeperException = Assertions
                .assertThrows(MinesweeperException.class, () -> gameValidator.checkIfFlipActionIsAllowed(cell2));
        Assertions.assertEquals(expectedException, minesWeeperException);

    }

    @Test
    public void checkIfGameAndUserMatches() {
        User user = new User();
        user.setId("fooId");
        Game game = Mockito.mock(Game.class);
        Mockito.when(game.getUserId()).thenReturn("barId");
        MinesweeperException expectedException = new MinesweeperException("USER_NOT_AUTHORIZED_TO_THIS_GAME",
                "The user cannot perform actions on this game", 403);
        Mockito.when(exceptionUtils.buildException("user.game.not.match.type", "user.game.not.match.description",
                "user.game.not.match.status")).thenReturn(expectedException);
        MinesweeperException minesWeeperException = Assertions
                .assertThrows(MinesweeperException.class, () -> gameValidator.checkIfGameAndUserMatches(game, user));
        Assertions.assertEquals(expectedException, minesWeeperException);

    }

    @Test
    public void checkIfUserExists() {

        MinesweeperException expectedException = new MinesweeperException("NON_EXISTENT_USER",
                "The username does not exists", 401);
        Mockito.when(exceptionUtils.buildException("user.not.existent.type", "user.not.existent.description",
                "user.not.existent.status")).thenReturn(expectedException);
        MinesweeperException minesWeeperException = Assertions
                .assertThrows(MinesweeperException.class, () -> gameValidator.checkIfUserExists(Optional.empty()));
        Assertions.assertEquals(expectedException, minesWeeperException);

    }

}
