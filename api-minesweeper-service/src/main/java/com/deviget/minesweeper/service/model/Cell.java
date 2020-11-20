package com.deviget.minesweeper.service.model;

import java.util.Objects;

public class Cell {

    private int row;
    private int column;
    private int minesAround;
    private boolean mine;
    private CellState state;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.mine = false;
        this.state = CellState.CLOSED;
        this.minesAround = 0;
    }

    public int getRow() {
        return row;
    }


    public int getColumn() {
        return column;
    }


    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }


    public int getMinesAround() {
        return minesAround;
    }

    public void incrementMinesAround(){
        this.minesAround+=1;
    }

    public CellState getState() {
        return state;
    }

    public void updateStatus(CellState state) {
        this.state = state;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell cell = (Cell) o;
        return row == cell.row &&
                column == cell.column &&
                minesAround == cell.minesAround &&
                mine == cell.mine &&
                state == cell.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, minesAround, mine, state);
    }
}
