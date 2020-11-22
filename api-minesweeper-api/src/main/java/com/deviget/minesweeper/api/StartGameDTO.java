package com.deviget.minesweeper.api;

import java.io.Serializable;
import java.util.Objects;

public class StartGameDTO implements Serializable {

    private int rows;
    private int columns;
    private int mines;

    public StartGameDTO(){}

    public StartGameDTO(int rows, int columns, int mines) {
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StartGameDTO)) return false;
        StartGameDTO that = (StartGameDTO) o;
        return rows == that.rows &&
                columns == that.columns &&
                mines == that.mines;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, columns, mines);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StartGameAction{");
        sb.append("row=").append(rows);
        sb.append(", columns=").append(columns);
        sb.append(", mines=").append(mines);
        sb.append('}');
        return sb.toString();
    }
}
