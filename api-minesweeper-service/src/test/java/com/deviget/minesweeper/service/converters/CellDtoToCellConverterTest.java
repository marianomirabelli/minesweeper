package com.deviget.minesweeper.service.converters;

import com.deviget.minesweeper.api.CellDTO;
import com.deviget.minesweeper.api.CellStateDTO;
import com.deviget.minesweeper.service.model.Cell;
import com.deviget.minesweeper.service.model.CellState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CellDtoToCellConverterTest {

    private CellToCellDTOConverter converter = new CellToCellDTOConverter();

    @Test
    @DisplayName("Should generate a dto cell with blank state")
    public void convertCellToDtoWithBlank(){

        CellDTO cellDTO = new CellDTO(3,4, 0,CellStateDTO.BLANK);
        Cell cell = new Cell(3,4);
        cell.updateStatus(CellState.OPENED);
        CellDTO convertedDtoCell = converter.convert(cell);
        Assertions.assertEquals(cellDTO,convertedDtoCell);
    }

    @Test
    @DisplayName("Should generate a dto cell with numbered state")
    public void convertCellToDtoWithNumbered(){

        CellDTO cellDTO = new CellDTO(3,4, 2,CellStateDTO.NUMBERED);
        Cell cell = new Cell(3,4);
        cell.incrementMinesAround();
        cell.incrementMinesAround();
        cell.updateStatus(CellState.OPENED);
        CellDTO convertedDtoCell = converter.convert(cell);
        Assertions.assertEquals(cellDTO,convertedDtoCell);
    }

    @Test
    @DisplayName("Should generate a dto cell with mine state")
    public void convertCellToDtoWithMine(){
        CellDTO cellDTO = new CellDTO(3,4, 0,CellStateDTO.MINE);
        Cell cell = new Cell(3,4);
        cell.updateStatus(CellState.OPENED);
        cell.setMine(true);
        CellDTO convertedDtoCell = converter.convert(cell);
        Assertions.assertEquals(cellDTO,convertedDtoCell);
    }

    @Test
    @DisplayName("Should generate a dto cell with flagged state")
    public void convertCellToDtoWithFlag(){
        CellDTO cellDTO = new CellDTO(3,4, 2,CellStateDTO.FLAGGED);
        Cell cell = new Cell(3,4);
        cell.incrementMinesAround();
        cell.incrementMinesAround();
        cell.updateStatus(CellState.FLAGGED);
        cell.setMine(true);
        CellDTO convertedDtoCell = converter.convert(cell);
        Assertions.assertEquals(cellDTO,convertedDtoCell);
    }

    @Test
    @DisplayName("Should generate a dto cell with marked state")
    public void convertCellToDtoWithMark(){
        CellDTO cellDTO = new CellDTO(3,4, 1,CellStateDTO.MARKED);
        Cell cell = new Cell(3,4);
        cell.incrementMinesAround();
        cell.updateStatus(CellState.MARKED);
        CellDTO convertedDtoCell = converter.convert(cell);
        Assertions.assertEquals(cellDTO,convertedDtoCell);
    }

    @Test
    @DisplayName("Should generate a dto cell with closed state")
    public void convertCellToDtoWithClosedState(){
        CellDTO cellDTO = new CellDTO(3,4, 0,CellStateDTO.CLOSED);
        Cell cell = new Cell(3,4);
        cell.updateStatus(CellState.CLOSED);
        CellDTO convertedDtoCell = converter.convert(cell);
        Assertions.assertEquals(cellDTO,convertedDtoCell);
    }

    @Test
    @DisplayName("Should generate a dto cell with closed state with mine")
    public void convertCellToDtoWithClosedStateWithMine(){
        CellDTO cellDTO = new CellDTO(3,4, 0,CellStateDTO.CLOSED);
        Cell cell = new Cell(3,4);
        cell.setMine(true);
        cell.updateStatus(CellState.CLOSED);
        CellDTO convertedDtoCell = converter.convert(cell);
        Assertions.assertEquals(cellDTO,convertedDtoCell);
    }
}
