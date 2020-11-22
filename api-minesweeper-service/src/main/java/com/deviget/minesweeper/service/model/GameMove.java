package com.deviget.minesweeper.service.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameMove)) return false;
        GameMove gameMove = (GameMove) o;
        return row == gameMove.row &&
                column == gameMove.column &&
                action == gameMove.action;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, action);
    }
}
