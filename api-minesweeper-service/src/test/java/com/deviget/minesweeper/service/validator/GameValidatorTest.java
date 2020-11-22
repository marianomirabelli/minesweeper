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
        GameException expectedException = new GameException("OPERATION_NOT_ALLOWED","The game has finished",406);
        Mockito.when(game.getStatus()).thenReturn(GameStatus.WON);
        Mockito.when(exceptionUtils.buildException("game.playable.type", "game.playable.description ",
                                                   "game.playable.status")).thenReturn(expectedException);
        GameException exceptionThrown = Assertions.assertThrows(GameException.class,
                                                () -> gameValidator.checkIfGameIsPlayable(game));

        Assertions.assertEquals(expectedException,exceptionThrown);

    }

    @Test
    public void checkIfGameExists() {
        GameException expectedException = new GameException("GAME_NOT_FOUND","The game does not exist",404);
        Mockito.when(exceptionUtils.buildException("game.not.found.type", "game.not.found.description", "game.not.found.status"))
                                    .thenReturn(expectedException);
        GameException exceptionThrown = Assertions.assertThrows(GameException.class,
                () -> gameValidator.checkIfGameExists(Optional.empty()));

        Assertions.assertEquals(expectedException,exceptionThrown);

    }

    @Test
    public void checkIfItIsAValidCoordinate() {
        Board board = Mockito.mock(Board.class);
        Mockito.when(board.getRows()).thenReturn(5);
        Mockito.when(board.getColumns()).thenReturn(5);
        GameException expectedException = new GameException("INVALID_COORDINATES","Coordinates are out of range",412);

        Mockito.when(exceptionUtils.buildException("game.coordinates.type", "game.coordinates.description", "game.coordinates.status"))
                .thenReturn(expectedException);

        Assertions.assertThrows(GameException.class,
                () -> gameValidator.checkIfItIsAValidCoordinate(new GameMove(8,9, GameAction.MARK),board));
        Assertions.assertThrows(GameException.class,
                () -> gameValidator.checkIfItIsAValidCoordinate(new GameMove(3,9, GameAction.MARK),board));

        GameException exceptionThrown = Assertions.assertThrows(GameException.class,
                () -> gameValidator.checkIfItIsAValidCoordinate(new GameMove(8,3, GameAction.MARK),board));
        Assertions.assertEquals(expectedException,exceptionThrown);
    }

    @Test
    public void checkIfGameCanBeCreated() {
        GameException expectedException = new GameException("MINES_NUMBER_EXCEEDED","The number of mines exceeds the maximum allowed",412);
        Mockito.when(exceptionUtils.buildException("game.mines.exceeded.type", "game.mines.exceeded.description", "game.mines.exceeded.status"))
                .thenReturn(expectedException);
        GameException gameException = Assertions.assertThrows(GameException.class,
                () -> gameValidator.checkIfGameCanBeCreated(5, 5, 20));
        Assertions.assertEquals(expectedException,gameException);
    }
}
