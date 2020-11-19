package com.deviget.minesweeper.service.model;

import java.util.*;

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
        init();
    }

    private void init() {
        this.cells = new Cell[rows][columns];
        this.initializeBoard();
        this.placeMines();
    }

    private void initializeBoard(){
        Queue<Cell> cellToInitialize = new LinkedList<>();
        Set<Cell> visitedCells = new HashSet<>();
        cellToInitialize.add(new Cell(0, 0));
        while(!cellToInitialize.isEmpty()){
            Cell cell = cellToInitialize.remove();
            if(!visitedCells.contains(cell)){
                visitedCells.add(cell);
                List<Cell> neighbours = this.initializeNeighbours(cell);
                cell.addNeighbours(neighbours);
                cellToInitialize.addAll(neighbours);
                if(Objects.isNull(this.cells[cell.getRow()][cell.getColumn()])){
                    this.cells[cell.getRow()][cell.getColumn()] = cell;
                }
            }
        }
    }

    private void placeMines(){
        int minesPlaces = 0;
        while(minesPlaces<this.numberOfMines){
            int row = (int)(Math.random() * this.rows);
            int column = (int)(Math.random() * this.columns);
            Cell currentCell = this.cells[row][column];
            if(!currentCell.isMine()){
                this.cells[row][column].setMine(true);
                this.cells[row][column].getNeighbours().stream().filter(c -> !c.isMine()).forEach(cell -> cell.incrementMinesAround());
                this.mines.add(this.cells[row][column]);
                minesPlaces++;
            }
        }
    }

    private List<Cell> initializeNeighbours(Cell cell) {
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

                Cell neighbourCell = this.cells[coordinates[0]][coordinates[1]];
                if(Objects.isNull(neighbourCell)){
                    neighbourCell = new Cell(coordinates[0],coordinates[1]);
                    this.cells[coordinates[0]][coordinates[1]] = neighbourCell;
                }
                neighboursCells.add(neighbourCell);
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
