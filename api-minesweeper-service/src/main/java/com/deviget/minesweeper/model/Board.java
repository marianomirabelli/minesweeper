package com.deviget.minesweeper.model;

import java.util.Random;

public class Board {

    private int rows;
    private int columns;
    private int mines;
    private Cell[][] cells;

    public Board(int rows, int columns, int mines) {
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
        initializeBoard();
    }


    private void initializeBoard() {
        this.cells = new Cell[rows][columns];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                this.cells[i][j] = new Cell(i, j);
            }
        }
        Random randomGenerator = new Random();
        for (int k = 0; k < this.mines; k++) {
            int row = randomGenerator.nextInt(this.rows);
            int column = randomGenerator.nextInt(this.columns);
            this.cells[row][column].setMine(true);
            this.incrementEdgeCellCounters(this.cells[row][column]);
        }

    }

    private void incrementEdgeCellCounters(Cell currentMineCell) {

        int rowPosition = currentMineCell.getRow();
        int columnPosition = currentMineCell.getColumn();

        int[][] neighbourCoordinates = {{rowPosition, columnPosition + 1},
                {rowPosition, columnPosition - 1},
                {rowPosition - 1, columnPosition - 1},
                {rowPosition - 1, columnPosition},
                {rowPosition - 1, columnPosition + 1},
                {rowPosition + 1, columnPosition - 1},
                {rowPosition + 1, columnPosition},
                {rowPosition + 1, columnPosition + 1}};

        for(int [] coordinates : neighbourCoordinates){
            Cell currentNeighbour = this.getCells()[coordinates[0]][coordinates[1]];
            if(isValidCell(currentNeighbour)){
                currentNeighbour.incrementMinesAround();
            }
        }
    }


    private boolean isValidCell(Cell cell) {

        if (cell.isMine()) {
            return false;
        }

        if ((cell.getRow() <= 0 || cell.getRow() >= this.rows) ||
             (cell.getColumn() <= 0 || cell.getColumn() >= this.columns)) {
            return false;
        }

        return true;
    }


    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

}
