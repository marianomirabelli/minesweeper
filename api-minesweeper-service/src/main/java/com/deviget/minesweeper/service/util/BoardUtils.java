package com.deviget.minesweeper.service.util;

import com.deviget.minesweeper.service.model.Board;
import com.deviget.minesweeper.service.model.Cell;
import com.deviget.minesweeper.service.model.CellState;

import java.util.*;

public class BoardUtils {

    public static void floodFlip(Board board, Cell sourceCell) {

        Queue<Cell> queue = new LinkedList();
        Set<Cell> visited = new HashSet<>();
        queue.add(sourceCell);
        while (!queue.isEmpty()) {
            Cell currentCell = queue.remove();
            if(!visited.contains(currentCell)){
                visited.add(currentCell);
                if (currentCell.isMine()) {
                    //TODO replace with correct exception management
                    flipMines(board);
                    throw new IllegalStateException("Mine detected");
                } else {
                    flipNeighbours(currentCell, board.getNeighbours(currentCell),queue);
                }
            }
        }
    }


    private static void flipMines(Board board) {

        List<Cell> mines = board.getMines();
        for (Cell mineCell : mines) {
            mineCell.updateStatus(CellState.OPENED);
        }
    }

    private static void flipNeighbours(Cell cell, List<Cell> neighbours,Queue queue) {
        cell.updateStatus(CellState.OPENED);
        if (cell.getMinesAround() == 0){
            for (Cell neighbour : neighbours) {
                if (!neighbour.isMine()) {
                    if (neighbour.getMinesAround() == 0) {
                        queue.add(neighbour);
                    }else{
                        neighbour.updateStatus(CellState.OPENED);
                    }
                }
            }
        }

    }
}
