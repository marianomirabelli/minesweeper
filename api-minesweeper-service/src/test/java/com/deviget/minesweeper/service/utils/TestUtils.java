package com.deviget.minesweeper.service.utils;

import com.deviget.minesweeper.service.model.Cell;

public class TestUtils {

    public static Cell[][] buildCells(int row, int column) {
        Cell[][] cells = new Cell[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
        return cells;
    }
}
