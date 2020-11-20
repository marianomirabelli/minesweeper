package com.deviget.minesweeper.service.converters;

import com.deviget.minesweeper.api.BoardDTO;
import com.deviget.minesweeper.api.CellDTO;
import com.deviget.minesweeper.api.CellStateDTO;
import com.deviget.minesweeper.service.model.Board;
import com.deviget.minesweeper.service.model.Cell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BoardConverterTest {

    private CellToCellDTOConverter cellToCellDTOConverter = Mockito.mock(CellToCellDTOConverter.class);
    private BoardToBoardDTOConverter boardToBoardDTOConverter = new BoardToBoardDTOConverter(cellToCellDTOConverter);


    @Test
    public void convertBoardToBoard() {
        Board board = new Board(3, 3, 2);
        BoardDTO boardDTO = new BoardDTO(3, 3, 2, createCells(3, 3, board.getCells()));
        BoardDTO convertedBoardDto = boardToBoardDTOConverter.convert(board);
        Mockito.verify(cellToCellDTOConverter, Mockito.times(9));
        assertBoard(boardDTO,convertedBoardDto);
    }

    private void assertBoard(BoardDTO expectedBoard, BoardDTO actualBoard){
        Assertions.assertEquals(expectedBoard.getRows(),actualBoard.getRows());
        Assertions.assertEquals(expectedBoard.getColumns(),actualBoard.getColumns());
        Assertions.assertEquals(expectedBoard.getNumberOfMines(),actualBoard.getNumberOfMines());
        CellDTO [][] expectedCellDto = expectedBoard.getCells();
        CellDTO [][] actualCellDto = actualBoard.getCells();
        Assertions.assertEquals(expectedCellDto.length,actualCellDto.length);
        for (int i = 0; i < expectedBoard.getRows(); i++) {
            for (int j = 0; j < expectedBoard.getColumns(); j++) {
                Assertions.assertEquals(expectedCellDto[i][j],actualCellDto[i][j]);
            }
        }
    }

    private CellDTO[][] createCells(int rows, int columns, Cell[][] boardCells) {

        CellDTO[][] cellsDto = new CellDTO[rows][columns];
        CellDTO mock;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                mock = Mockito.mock(CellDTO.class);
                cellsDto[i][j] = mock;
                Mockito.when(cellToCellDTOConverter.convert(boardCells[i][j]))
                        .thenReturn(mock);
            }
        }
        return cellsDto;
    }
}
