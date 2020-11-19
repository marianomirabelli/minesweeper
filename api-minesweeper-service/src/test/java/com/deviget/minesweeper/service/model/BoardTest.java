package com.deviget.minesweeper.service.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BoardTest {

    @Test
    @DisplayName("Basic Board Creation")
    public void createBoard() {
        Board board = new Board(3, 3, 4);
        Assertions.assertEquals(4, board.getMines().size());
    }

    @Test
    @DisplayName("Validate Counters Around Mines Cells")
    public void validateCountersAroundMines() {
        Board board = new Board(3, 3, 2);
        Iterator<Cell> it = board.getMines().iterator();
        Cell mine1 = it.next();
        Cell mine2 = it.next();
        Set<Cell> notMineNeighbours = new HashSet<>(mine1.getNeighbours().stream().filter(c -> !c.isMine()).collect(Collectors.toList()));
        Set<Cell> notMineNeighbours2 = new HashSet(mine2.getNeighbours().stream().filter(c -> !c.isMine()).collect(Collectors.toList()));

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
        List<Cell> neighbours = cell.getNeighbours();
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


    private void assertNeighbourCoordinates(int row, int column, Cell neighbour){
        Assertions.assertEquals(row,neighbour.getRow());
        Assertions.assertEquals(column,neighbour.getColumn());
    }

}
