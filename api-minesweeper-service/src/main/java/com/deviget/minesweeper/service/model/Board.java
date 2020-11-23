package com.deviget.minesweeper.service.model;

import java.util.*;

public class Board {

    private int rows;
    private int columns;
    private int numberOfMines;
    private int openedCells;
    private Cell[][] cells;
    private List<Cell> mines;

    public Board() {
    }

    public Board(int rows, int columns, int numberOfMines) {
        this.rows = rows;
        this.columns = columns;
        this.openedCells = 0;
        this.numberOfMines = numberOfMines;
        this.mines = new ArrayList<>(numberOfMines);
        this.initializeBoard();
    }

    // Constructor with testing purpose
    public Board(Cell[][] cells, int[][] minesCoordinates) {
        this.cells = cells;
        this.rows = cells.length;
        this.columns = cells[0].length;
        this.mines = new ArrayList<>(minesCoordinates.length);
        this.numberOfMines = minesCoordinates.length;
        this.initializeMinesWithCoordinates(minesCoordinates);
    }

    /**
     * This method returns the neighbors of the cell.
     * The neighbors are usually eight but could be fewer if it is a border cell.
     * @param cell
     * @return
     */
    public List<Cell> getNeighbours(Cell cell) {
        int rowPosition = cell.getRow();
        int columnPosition = cell.getColumn();

        int[][] neighbourCoordinates = { { rowPosition, columnPosition + 1 },
                { rowPosition, columnPosition - 1 },
                { rowPosition - 1, columnPosition - 1 },
                { rowPosition - 1, columnPosition },
                { rowPosition - 1, columnPosition + 1 },
                { rowPosition + 1, columnPosition - 1 },
                { rowPosition + 1, columnPosition },
                { rowPosition + 1, columnPosition + 1 } };

        final List<Cell> neighboursCells = new ArrayList<>();

        for (int[] coordinates : neighbourCoordinates) {
            if ((coordinates[0] >= 0 && coordinates[0] < this.rows) &&
                    (coordinates[1] >= 0 && coordinates[1] < this.columns)) {
                neighboursCells.add(this.cells[coordinates[0]][coordinates[1]]);
            }
        }
        return neighboursCells;
    }

    /**
     * Algorithm based on BFS algorithm. When a blank cell is detected,
     * the algorithm is going through all blank neighbours until reach a not blank cell.Additionally,
     * this algorithm detects mines cells. When a mine is detected, all board  mines are revealed and
     * the algorithm will finished.
     * @param cell
     */
    public void floodFlip(Cell cell) {
        Queue<Cell> queue = new LinkedList();
        Set<Cell> visited = new HashSet<>();
        queue.add(cell);
        boolean mineNotFound = true;
        while (!queue.isEmpty() && mineNotFound) {
            Cell currentCell = queue.remove();
            if (visited.add(currentCell)) {
                if (currentCell.isMine()) {
                    flipMines();
                    mineNotFound = false;
                } else {
                    flipNeighbours(currentCell, queue);
                }
            }
        }
    }

    /**
     * This method populates the mines in the board. Mines are randomly placed
     * avoiding open cells as a result of the first user move on the board.
     * @param firstMovementRow
     * @param firstMovementColumn
     */
    public void initializeMines(int firstMovementRow, int firstMovementColumn) {
        int minesPlaces = 0;
        this.mines = new ArrayList<>(numberOfMines);
        while (minesPlaces < this.numberOfMines) {
            int row = (int) (Math.random() * this.rows);
            int column = (int) (Math.random() * this.columns);
            Cell currentCell = this.cells[row][column];
            if (!currentCell.isMine()
                    && (row != firstMovementRow && column != firstMovementColumn)
                    && (!currentCell.getState().equals(CellState.OPENED))) {
                this.placeMine(row, column);
                minesPlaces++;
            }
        }
    }

    public int getOpenedCells() {
        return openedCells;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getNumberOfMines() {
        return numberOfMines;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public List<Cell> getMines() {
        return mines;
    }

    private void initializeBoard() {
        this.cells = new Cell[rows][columns];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                this.cells[i][j] = new Cell(i, j);
            }
        }
    }

    private void initializeMinesWithCoordinates(int[][] coordinates) {
        for (int[] coordinate : coordinates) {
            this.placeMine(coordinate[0], coordinate[1]);
        }
    }


    private void placeMine(int row, int column) {
        this.cells[row][column].setMine(true);
        this.getNeighbours(this.cells[row][column])
                .stream().filter(c -> !c.isMine()).forEach(cell -> cell.incrementMinesAround());
        this.mines.add(this.cells[row][column]);
    }

    private void flipMines() {
        List<Cell> mines = this.getMines();
        for (Cell mineCell : mines) {
            mineCell.updateStatus(CellState.OPENED);
            this.cells[mineCell.getRow()][mineCell.getColumn()].updateStatus(CellState.OPENED);
        }
    }

    private void flipNeighbours(Cell cell, Queue queue) {
        List<Cell> neighbours = this.getNeighbours(cell);
        this.openedCells++;
        cell.updateStatus(CellState.OPENED);
        if (cell.getMinesAround() == 0) {
            for (Cell neighbour : neighbours) {
                if (!neighbour.isMine()) {
                    queue.add(neighbour);
                }
            }
        }
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Board))
            return false;
        Board board = (Board) o;
        return rows == board.rows &&
                columns == board.columns &&
                numberOfMines == board.numberOfMines &&
                Arrays.equals(cells, board.cells) &&
                Objects.equals(mines, board.mines);
    }

    @Override public int hashCode() {
        int result = Objects.hash(rows, columns, numberOfMines, openedCells, mines);
        result = 31 * result + Arrays.hashCode(cells);
        return result;
    }
}
