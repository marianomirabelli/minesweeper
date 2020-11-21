package com.deviget.minesweeper.service.utils;

import com.deviget.minesweeper.service.model.Board;
import com.deviget.minesweeper.service.model.Cell;

public class Printer {

    public static void printBoard(Board board){
        Cell[][] cells = board.getCells();
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                System.out.print(cells[i][j].getState());
                System.out.print("\t");
            }
            System.out.println("\n");
        }
    }
}
