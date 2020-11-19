package com.deviget.minesweeper.service.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
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
}
