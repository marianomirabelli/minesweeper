package com.deviget.minesweeper.service.model;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private int rows;
    private int columns;
    private int numberOfMines;
    private Cell[][] cells;
    private List<Cell> mines;

    public Board(int rows, int columns, int numberOfMines) {
        this.rows = rows;
        this.columns = columns;
        this.numberOfMines = numberOfMines;
        this.mines = new ArrayList<>(numberOfMines);
        initializeBoard();
    }

    private void initializeBoard() {
        this.cells = new Cell[rows][columns];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                this.cells[i][j] = new Cell(i, j);
            }
        }
        int minesPlaces = 0;
        while(minesPlaces<this.numberOfMines){
            int row = (int)(Math.random() * this.rows);
            int column = (int)(Math.random() * this.columns);
            Cell currentCell = this.cells[row][column];
            if(!currentCell.isMine()){
                this.cells[row][column].setMine(true);
                this.mines.add(this.cells[row][column]);
                this.neighbours(this.cells[row][column]).stream().filter(c -> !c.isMine()).forEach(cell -> cell.incrementMinesAround());
                minesPlaces++;
            }
        }
    }

    public List<Cell> neighbours(Cell cell) {
        int rowPosition = cell.getRow();
        int columnPosition = cell.getColumn();

        int[][] neighbourCoordinates = {{rowPosition, columnPosition + 1},
                {rowPosition, columnPosition - 1},
                {rowPosition - 1, columnPosition - 1},
                {rowPosition - 1, columnPosition},
                {rowPosition - 1, columnPosition + 1},
                {rowPosition + 1, columnPosition - 1},
                {rowPosition + 1, columnPosition},
                {rowPosition + 1, columnPosition + 1}};

        final List<Cell> neighboursCells = new ArrayList<>();
        for (int[] coordinates : neighbourCoordinates) {
            if ((coordinates[0] >= 0 && coordinates[0] < this.rows) &&
                    (coordinates[1] >= 0 && coordinates[1] < this.columns)) {
                neighboursCells.add(this.cells[coordinates[0]][coordinates[1]]);
            }
        }

        return neighboursCells;

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

    public int getNumberOfMines() {
        return numberOfMines;
    }

    public void setNumberOfMines(int numberOfMines) {
        this.numberOfMines = numberOfMines;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public List<Cell> getMines() {
        return mines;
    }


}
