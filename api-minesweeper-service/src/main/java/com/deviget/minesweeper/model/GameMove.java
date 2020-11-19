package com.deviget.minesweeper.model;

public class GameMove {

    private int row;
    private int column;
    private CellAction action;

    public GameMove(int row, int column, CellAction action) {
        this.row = row;
        this.column = column;
        this.action = action;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public CellAction getAction() {
        return action;
    }

    public void setAction(CellAction action) {
        this.action = action;
    }
}
