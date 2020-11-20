package com.deviget.minesweeper.service.model;

public class GameMove {

    private int row;
    private int column;
    private GameAction action;

    public GameMove(int row, int column, GameAction action) {
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

    public GameAction getAction() {
        return action;
    }

    public void setAction(GameAction action) {
        this.action = action;
    }
}
