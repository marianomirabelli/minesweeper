package com.deviget.minesweeper.service.model;

import com.deviget.minesweeper.service.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class BoardTest {

    @Test
    @DisplayName("Check counters around mines cells")
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

        for (Cell commonNeighbour : intersection) {
            Assertions.assertEquals(2, commonNeighbour.getMinesAround());
        }

        for (Cell uniqueNeighbour : symmetricDifference) {
            Assertions.assertEquals(1, uniqueNeighbour.getMinesAround());
        }

    }

    @Test
    @DisplayName("Check neighbours cells are correctly initialized")
    public void validateNeighboursAreCorrectlyInitialized() {
        Board board = new Board(3, 3, 2);
        Cell cell = board.getCells()[1][1];
        Cell[][] cells = board.getCells();
        List<Cell> neighbours = board.getNeighbours(cell);
        Assertions.assertEquals(8, neighbours.size());
        Assertions.assertEquals(cells[1][2], neighbours.get(0));
        Assertions.assertEquals(cells[1][0], neighbours.get(1));
        Assertions.assertEquals(cells[0][0], neighbours.get(2));
        Assertions.assertEquals(cells[0][1], neighbours.get(3));
        Assertions.assertEquals(cells[0][2], neighbours.get(4));
        Assertions.assertEquals(cells[2][0], neighbours.get(5));
        Assertions.assertEquals(cells[2][1], neighbours.get(6));
        Assertions.assertEquals(cells[2][2], neighbours.get(7));
    }

    @Test
    @DisplayName("Flip not mine cell")
    public void flipNotMineCell() {
        Cell[][] cells = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = {{1, 2}, {4, 4}, {3, 0}};
        Board board = new Board(cells, minesCoordinates);
        board.floodFlip(cells[0][0]);
        Set<Cell> openedCells = new HashSet<>();
        openedCells.add(cells[0][0]);
        openedCells.add(cells[0][1]);
        openedCells.add(cells[1][0]);
        openedCells.add(cells[1][1]);
        openedCells.add(cells[2][0]);
        openedCells.add(cells[2][1]);
        Assertions.assertEquals(6,board.getOpenedCells());
        assertOpenedClosedCells(board,cells,openedCells);
    }

    @Test
    @DisplayName("Flip not mine numbered cell")
    public void flipNotMineNumberedCell() {
        Cell[][] cells = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = {{1, 2}, {4, 4}, {3, 0}};
        Board board = new Board(cells, minesCoordinates);
        board.floodFlip(cells[1][3]);
        Set<Cell> openedCells = new HashSet<>();
        openedCells.add(cells[1][3]);
        Assertions.assertEquals(1,board.getOpenedCells());
        assertOpenedClosedCells(board,cells,openedCells);
    }

    @Test
    @DisplayName("Flip mine cell")
    public void flipMineCell() {
        Cell[][] cells = TestUtils.buildCells(5, 5);
        int[][] minesCoordinates = {{1, 2}, {4, 4}, {3, 0}};
        Board board = new Board(cells, minesCoordinates);
        board.floodFlip(cells[4][4]);
        for (Cell minesCell : board.getMines()) {
                Assertions.assertEquals(CellState.OPENED, minesCell.getState());
                Assertions.assertEquals(CellState.OPENED,cells[minesCell.getRow()][minesCell.getColumn()].getState());
        }
    }


    private void assertOpenedClosedCells(Board board, Cell[][] cells, Set<Cell> openedCells){
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                Cell currentCell = cells[i][j];
                if(openedCells.contains(currentCell)){
                    Assertions.assertEquals(CellState.OPENED,currentCell.getState());
                }else{
                    Assertions.assertEquals(CellState.CLOSED,currentCell.getState());
                }
            }
        }
    }




}
