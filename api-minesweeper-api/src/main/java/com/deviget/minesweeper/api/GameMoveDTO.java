package com.deviget.minesweeper.api;

import java.util.Objects;

public class GameMoveDTO {

    private int row;
    private int column;
    private GameActionDTO action;

    public GameMoveDTO(){}

    public GameMoveDTO(int row, int column, GameActionDTO action) {
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

    public GameActionDTO getAction() {
        return action;
    }

    public void setAction(GameActionDTO action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameMoveDTO)) return false;
        GameMoveDTO that = (GameMoveDTO) o;
        return row == that.row &&
                column == that.column &&
                action == that.action;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, action);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GameMoveDTO{");
        sb.append("row=").append(row);
        sb.append(", column=").append(column);
        sb.append(", action=").append(action);
        sb.append('}');
        return sb.toString();
    }
}
