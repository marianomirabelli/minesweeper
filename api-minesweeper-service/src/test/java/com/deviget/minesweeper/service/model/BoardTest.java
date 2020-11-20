package com.deviget.minesweeper.service.model;

import com.deviget.minesweeper.service.util.BoardUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class BoardTest {

    @Test
    @DisplayName("Validate Counters Around Mines Cells")
    public void validateCountersAroundMines() {
        Board board = new Board(3, 3, 2);
        Iterator<Cell> it = board.getMines().iterator();
        Cell mine1 = it.next();
        Cell mine2 = it.next();
        Set<Cell> notMineNeighbours = new HashSet<>(board.getNeighbours(mine1).stream()
                                                    .filter(c -> !c.isMine()).collect(Collectors.toList()));
        Set<Cell> notMineNeighbours2 = new HashSet(board.getNeighbours(mine2).stream()
                                                    .filter(c -> !c.isMine()).collect(Collectors.toList()));

        Set<Cell> intersection = new HashSet(notMineNeighbours);
        intersection.retainAll(notMineNeighbours2);

        Set<Cell> symmetricDifference = new HashSet(notMineNeighbours);
        symmetricDifference.addAll(notMineNeighbours2);
        symmetricDifference.removeAll(intersection);

       for(Cell commonNeighbour:intersection){
            Assertions.assertEquals(2,commonNeighbour.getMinesAround());
        }

        for(Cell uniqueNeighbour:symmetricDifference){
            Assertions.assertEquals(1,uniqueNeighbour.getMinesAround());
        }

    }

    @Test
    @DisplayName("Validate Neighoburs Cells are correctly initialized")
    public void validateNeighboursAreCorrectlyInitialized(){
        Board board = new Board(3, 3, 2);
        Cell cell = board.getCells()[1][1];
        List<Cell> neighbours = board.getNeighbours(cell);
        Assertions.assertEquals(8,neighbours.size());
        assertNeighbourCoordinates(1,2,neighbours.get(0));
        assertNeighbourCoordinates(1,0,neighbours.get(1));
        assertNeighbourCoordinates(0,0,neighbours.get(2));
        assertNeighbourCoordinates(0,1,neighbours.get(3));
        assertNeighbourCoordinates(0,2,neighbours.get(4));
        assertNeighbourCoordinates(2,0,neighbours.get(5));
        assertNeighbourCoordinates(2,1,neighbours.get(6));
        assertNeighbourCoordinates(2,2,neighbours.get(7));
    }


    @Test
    @DisplayName("Validate Flood Flip algorithm without click mines")
    public void validateFloodFlipWithoutMines(){
        Cell[][] cells = new Cell[5][5];
        for(int i= 0; i<5;i++){
            for(int j= 0; j<5;j++){
                cells[i][j] = new Cell(i,j);
            }
        }
        cells[1][2].setMine(true);
        cells[4][4].setMine(true);
        cells[3][0].setMine(true);
        int[][] minesCoordinates = {{1, 2},{4, 4},{3,0}};
        Board board = new Board(cells,minesCoordinates);
        BoardUtils.floodFlip(board,cells[0][0]);
        Assertions.assertEquals(CellState.OPENED,cells[0][0].getState());
        Assertions.assertEquals(CellState.OPENED,cells[0][1].getState());
        Assertions.assertEquals(CellState.OPENED,cells[1][0].getState());
        Assertions.assertEquals(CellState.OPENED,cells[1][1].getState());
        Assertions.assertEquals(CellState.OPENED,cells[2][0].getState());
        Assertions.assertEquals(CellState.OPENED,cells[2][1].getState());
        Assertions.assertEquals(CellState.CLOSED,cells[0][2].getState());
        Assertions.assertEquals(CellState.CLOSED,cells[1][2].getState());
        Assertions.assertEquals(CellState.CLOSED,cells[0][3].getState());
    }



    private void assertNeighbourCoordinates(int row, int column, Cell neighbour){
        Assertions.assertEquals(row,neighbour.getRow());
        Assertions.assertEquals(column,neighbour.getColumn());
    }

}
