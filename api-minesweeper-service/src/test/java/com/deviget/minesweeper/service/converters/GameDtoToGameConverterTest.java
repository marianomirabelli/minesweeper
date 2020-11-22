package com.deviget.minesweeper.service.converters;

import com.deviget.minesweeper.api.BoardDTO;
import com.deviget.minesweeper.api.GameDTO;
import com.deviget.minesweeper.api.GameStatusDTO;
import com.deviget.minesweeper.service.model.Board;
import com.deviget.minesweeper.service.model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;

public class GameDtoToGameConverterTest {

    private BoardToBoardDTOConverter boardToBoardDTOConverter = Mockito.mock(BoardToBoardDTOConverter.class);
    private GameToGameDTOConverter gameToGameDTOConverter = new GameToGameDTOConverter(boardToBoardDTOConverter);

    @Test
    public void covertGameToGameDto(){
        BoardDTO expectedBoardDTO = Mockito.mock(BoardDTO.class);
        Board board = Mockito.mock(Board.class);
        Game game = new Game(board,"fooUserId");
        GameDTO expectedGameDto = new GameDTO("gameId","fooUserId",expectedBoardDTO, 0, game.getStartTime(),
                                              game.getLastMove(),GameStatusDTO.PLAYING);
        game.setId("gameId");
        Mockito.when(boardToBoardDTOConverter.convert(board)).thenReturn(expectedBoardDTO);
        GameDTO actualGameDto = gameToGameDTOConverter.convert(game);
        Mockito.verify(boardToBoardDTOConverter,Mockito.times(1)).convert(board);
        Assertions.assertEquals(expectedGameDto,actualGameDto);
    }
}
