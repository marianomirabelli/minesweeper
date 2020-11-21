package com.deviget.minesweeper.service.converters;

import com.deviget.minesweeper.api.BoardDTO;
import com.deviget.minesweeper.api.GameDTO;
import com.deviget.minesweeper.api.GameStatusDTO;
import com.deviget.minesweeper.service.model.Board;
import com.deviget.minesweeper.service.model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GameDtoToGameConverterTest {

    private BoardToBoardDTOConverter boardToBoardDTOConverter = Mockito.mock(BoardToBoardDTOConverter.class);
    private GameToGameDTOConverter gameToGameDTOConverter = new GameToGameDTOConverter(boardToBoardDTOConverter);

    @Test
    public void covertGameToGameDto(){
        BoardDTO expectedBoardDTO = Mockito.mock(BoardDTO.class);
        Board board = Mockito.mock(Board.class);
        GameDTO expectedGameDto = new GameDTO("fooId",expectedBoardDTO, GameStatusDTO.PLAYING);
        Game game = new Game(board,"fooId");
        game.setId("fooId");
        Mockito.when(boardToBoardDTOConverter.convert(board)).thenReturn(expectedBoardDTO);
        GameDTO actualGameDto = gameToGameDTOConverter.convert(game);
        Mockito.verify(boardToBoardDTOConverter.convert(board),Mockito.times(1));
        Assertions.assertEquals(expectedGameDto,actualGameDto);
    }
}
