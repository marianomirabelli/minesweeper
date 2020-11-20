package com.deviget.minesweeper.api;

import java.io.Serializable;
import java.util.Objects;

public class StartGameAction implements Serializable {

    private int row;
    private int columns;
    private int mines;

    public StartGameAction(){}

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
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
        if (!(o instanceof StartGameAction)) return false;
        StartGameAction that = (StartGameAction) o;
        return row == that.row &&
                columns == that.columns &&
                mines == that.mines;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, columns, mines);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StartGameAction{");
        sb.append("row=").append(row);
        sb.append(", columns=").append(columns);
        sb.append(", mines=").append(mines);
        sb.append('}');
        return sb.toString();
    }
}
