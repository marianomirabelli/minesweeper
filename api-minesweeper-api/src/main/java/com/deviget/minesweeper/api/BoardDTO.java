package com.deviget.minesweeper.api;

import java.util.Arrays;
import java.util.Objects;

public class BoardDTO {

    private int rows;
    private int columns;
    private int numberOfMines;
    private CellDTO[][] cells;

    public BoardDTO(){}

    public BoardDTO(int rows, int columns, int numberOfMines, CellDTO[][] cells){
        this.rows = rows;
        this.columns = columns;
        this.numberOfMines = numberOfMines;
        this.cells = cells;
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

    public CellDTO[][] getCells() {
        return cells;
    }

    public void setCells(CellDTO[][] cells) {
        this.cells = cells;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardDTO)) return false;
        BoardDTO boardDTO = (BoardDTO) o;
        return rows == boardDTO.rows &&
                columns == boardDTO.columns &&
                numberOfMines == boardDTO.numberOfMines &&
                Arrays.equals(cells, boardDTO.cells);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, columns, numberOfMines);
        result = 31 * result + Arrays.hashCode(cells);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BoardDTO{");
        sb.append("rows=").append(rows);
        sb.append(", columns=").append(columns);
        sb.append(", numberOfMines=").append(numberOfMines);
        sb.append(", cells=").append(Arrays.toString(cells));
        sb.append('}');
        return sb.toString();
    }
}
