package com.deviget.minesweeper.service.converters;

import com.deviget.minesweeper.api.BoardDTO;
import com.deviget.minesweeper.api.CellDTO;
import com.deviget.minesweeper.service.model.Board;
import com.deviget.minesweeper.service.model.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BoardToBoardDTOConverter implements Converter<Board, BoardDTO> {

    private final CellToCellDTOConverter cellToCellDTOConverter;

    @Autowired
    public BoardToBoardDTOConverter(CellToCellDTOConverter cellToCellDTOConverter) {
        this.cellToCellDTOConverter = cellToCellDTOConverter;
    }

    @Override
    public BoardDTO convert(Board board) {
        int rowsDto = board.getRows();
        int columnsDto = board.getColumns();
        int numberOfMinesDto = board.getNumberOfMines();
        CellDTO[][] cellDto = convertCells(board.getCells(), board.getRows(),board.getColumns());
        return new BoardDTO(rowsDto,columnsDto,numberOfMinesDto,cellDto);
    }

    private CellDTO[][] convertCells(Cell[][] cells, int rows, int columns) {
        CellDTO[][] cellDTOS = new CellDTO[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cellDTOS[i][j] = cellToCellDTOConverter.convert(cells[i][j]);
            }
        }
        return cellDTOS;
    }
}
